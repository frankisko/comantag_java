package com.frankisko.comantag.services;


import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.frankisko.comantag.config.Globals;
import com.frankisko.comantag.dao.MediaMapper;
import com.frankisko.comantag.dto.MediaDB;
import com.frankisko.comantag.dto.MediaInfo;
import com.frankisko.comantag.dto.MediaRatingRecord;
import com.frankisko.comantag.dto.MediaStatistic;
import com.frankisko.comantag.dto.MediaToScrap;
import com.frankisko.comantag.dto.GalleryMedia;
import com.frankisko.comantag.entities.Media;

import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;

@Service
public class MediaService {
    @Value("${viewer}")
    private String viewer;

    @Value("${data_directory}")
    private String dataDirectory;

    private final LocationService locationService;

    private final MediaMetadataService mediaMetadataService;

    private final MediaMapper mediaMapper;

    private final Globals globals;

    public MediaService(LocationService locationService, MediaMetadataService mediaMetadataService,
                        MediaMapper mediaMapper, Globals globals) {
        this.locationService = locationService;
        this.mediaMetadataService = mediaMetadataService;
        this.mediaMapper = mediaMapper;
        this.globals = globals;
    }

    public List<MediaStatistic> getStatistics() {
        return mediaMapper.getStatistics(globals.getIdCollection());
    }

    public MediaInfo getInfo(Integer idMedia) {
        return mediaMapper.getInfo(idMedia);
    }

    public void open(String name) {
        try {
            //open media
            ProcessBuilder processBuilder = new ProcessBuilder();
            List<String> command = new ArrayList<>();
            command.add(viewer);
            command.add(name);
            processBuilder.command(command);

            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void view(Integer idMedia) {
        mediaMapper.view(idMedia);
    }

    public void rating(Integer idMedia, MediaRatingRecord record) {
        Media media = new Media();
        media.setIdMedia(idMedia);
        media.setRating(record.getRating());

        mediaMapper.rating(media);
    }

    public List<GalleryMedia> getGalleryMedia(Map<String, Object> filters) {
        return mediaMapper.getGalleryMedia(globals.getIdCollection(), filters);
    }

    public List<MediaDB> getMediaDB() {
        return mediaMapper.getMediaDB(globals.getIdCollection());
    }

    public void scrapMedia(Integer idMedia) {
        mediaMapper.scrapMedia(idMedia);
    }

    public void scrapMediaBulk(List<Integer> idsMedia) {
        mediaMapper.scrapMediaBulk(idsMedia);
    }

    public void unscrapMedia(Integer idMedia) {
        this.deleteThumbnail(idMedia);
        mediaMapper.unscrapMedia(idMedia);
    }

    public void unscrapAll() {
        mediaMapper.unscrapAll(globals.getIdCollection());
    }

    public void delete(Integer idMedia) {
        mediaMetadataService.deleteByIdMedia(idMedia);
        mediaMapper.delete(idMedia);
    }

    public void deleteByIdCollection(Integer idCollection) {
        mediaMapper.deleteByIdCollection(idCollection);
    }

    public void deleteThumbnail(Integer idMedia) {
        Path thumbnailMediaPath = locationService.getAppDataThumbnailMediaPath(globals.getIdCollection(), idMedia);
        File file = thumbnailMediaPath.toFile();
        file.delete();
    }

    public Resource getThumbnail(Path thumbnailPath) {
        Resource resource = null;

        // Serve the image as a resource
        try {
            resource = new UrlResource(thumbnailPath.toUri());
        } catch (Exception e) {

        }
        return resource;

    }

    public void insert(Media media) {
        mediaMapper.insert(media);
    }

    public Integer countMediaInCollection(Integer idCollection) {
        return mediaMapper.countMediaInCollection(idCollection);
    }

    public Integer countPendingMediaInCollection(Integer idCollection) {
        return mediaMapper.countPendingMediaInCollection(idCollection);
    }

    public MediaToScrap getMediaToScrap(Integer idCollection) {
        return mediaMapper.getMediaToScrap(idCollection);
    }

    public void markAsProcessed(Media media) {
        mediaMapper.markAsProcessed(media);
    }

    public String processMedia(Integer idCollection) {
        String response = "";

        MediaToScrap mediaToScrap = this.getMediaToScrap(idCollection);

        Integer pages = 0;

        if (mediaToScrap != null) {
            response = mediaToScrap.getName();

            Path mediaToScrapPath = Paths.get(mediaToScrap.getLocation(), File.separator, mediaToScrap.getName());
            String mediaToScrapName = mediaToScrapPath.toString();

            if (isValidExtension(mediaToScrapName)) {
                //list contents
                String firstMedia = null;

                RandomAccessFile randomAccessFile = null;
                IInArchive inArchive = null;

                try {
                    randomAccessFile = new RandomAccessFile(mediaToScrapName, "r");
                    inArchive = SevenZip.openInArchive(null, // autodetect archive type
                                                       new RandomAccessFileInStream(randomAccessFile));

                    // Getting simple interface of the archive inArchive
                    ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();

                    for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                        if (item.getPath().endsWith(".jpg") || item.getPath().endsWith(".jpeg") || item.getPath().endsWith(".png") || item.getPath().endsWith(".webp"))  {
                            if (firstMedia == null) {
                                firstMedia = item.getPath();

                                String extension = getExtension(item.getPath());

                                File tempFile = Paths.get(locationService.getAppDataTmpPath().toString(), File.separator, mediaToScrap.getIdMedia().toString() + extension).toFile();

                                //extract file
                                try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile))) {
                                    item.extractSlow(new ISequentialOutStream() {
                                        @Override
                                        public int write(byte[] data) throws SevenZipException {
                                            try {
                                                outputStream.write(data);
                                                return data.length;
                                            } catch (IOException e) {
                                                throw new SevenZipException(e);
                                            }
                                        }
                                    });
                                }

                                createThumbnail(idCollection, mediaToScrap.getIdMedia(), tempFile);

                                // Clean up temporary files
                                tempFile.delete();
                            }

                            pages++;
                        }
                    }

                    //image not fond
                    if (firstMedia == null) {
                        Path originalPath = Paths.get("src/main/resources/static/images/no_preview.jpg");
                        Path targetPath = locationService.getAppDataThumbnailMediaPath(idCollection, mediaToScrap.getIdMedia());

                        Files.copy(originalPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (Exception e) {
                    System.err.println("Error occurs: " + e);
                } finally {
                    if (inArchive != null) {
                        try {
                            inArchive.close();
                        } catch (SevenZipException e) {
                            System.err.println("Error closing archive: " + e);
                        }
                    }
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (IOException e) {
                            System.err.println("Error closing file: " + e);
                        }
                    }
                }
            }

            File file = mediaToScrapPath.toFile();

            Media processedMedia = new Media();
            processedMedia.setIdMedia(mediaToScrap.getIdMedia());
            processedMedia.setSize(file.length());
            processedMedia.setPages(pages);
            processedMedia.setScrapped(1);

            this.markAsProcessed(processedMedia);
        } else {
            //TODO delete tmp folder
        }

        return response;
    }

    private String getExtension(String filename) {
        String extension = "";

        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i);
        }

        return extension;
    }

    private void createThumbnail(Integer idCollection, Integer idMedia, File file) {
        // Load the image and convert to JPEG if needed
        try {
            BufferedImage image = ImageIO.read(file);

            Integer targetWidth = 300;
            Integer targetHeight = (int) ((double) image.getHeight() / image.getWidth() * targetWidth);

            File targetFilePath = locationService.getAppDataThumbnailMediaPath(idCollection, idMedia).toFile();

            BufferedImage resizedImage = Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);

            ImageIO.write(resizedImage, "jpg", targetFilePath);

            resizedImage.flush();
            image.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidExtension(String filename) {
        filename = filename.toLowerCase();

        if (filename.endsWith(".cbz") || filename.endsWith(".zip") || filename.endsWith(".cbr") || filename.endsWith(".rar")) {
            return true;
        }

        return false;
    }

}
