package Utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CloudinaryUtil {

    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dnm1hfesq",
            "api_key", "862991173185198",
            "api_secret", "CT2gBG4nvlf3cGJJSUhcH40midU"
    ));

    public static String uploadImage(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        
        String originalFileName = filePart.getSubmittedFileName();
        String cleanedName = originalFileName.replaceAll("[^a-zA-Z0-9.\\-_]", "_");

        
        String projectPath = System.getProperty("user.dir"); 
        File uploadDir = new File(projectPath + "/web/uploads_tmp");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        
        File tempFile = new File(uploadDir, "upload-" + System.currentTimeMillis() + "-" + cleanedName);
        try (var input = filePart.getInputStream()) {
            java.nio.file.Files.copy(input, tempFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        
        Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap("folder", "products"));

        
        tempFile.delete();

        return (String) uploadResult.get("secure_url");
    }

    public static void deleteImage(String imageUrl) {
        try {
            if (imageUrl != null && imageUrl.contains("cloudinary")) {
                String publicId = extractPublicId(imageUrl);
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String extractPublicId(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String fileName = parts[parts.length - 1];
        return "products/" + fileName.substring(0, fileName.lastIndexOf('.'));
    }
}
