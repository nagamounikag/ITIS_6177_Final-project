import com.microsoft.azure.cognitiveservices.vision.computervision.*;
import com.microsoft.azure.cognitiveservices.vision.computervision.implementation.ComputerVisionImpl;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.*;

import java.io.*;
import java.nio.file.Files;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageAnalysisQuickstart {

    static String subscriptionKey = "*****";
    static String endpoint = "https://******.cognitiveservices.azure.com/";

    public static void main(String[] args) {
        
        System.out.println("\nAzure Cognitive Services Computer Vision - Java Quickstart Sample");

        // Create an authenticated Computer Vision client.
        ComputerVisionClient compVisClient = Authenticate(subscriptionKey, endpoint); 

        // Analyze local and remote images
        AnalyzeRemoteImage(compVisClient);

    }

    public static ComputerVisionClient Authenticate(String subscriptionKey, String endpoint){
        return ComputerVisionManager.authenticate(subscriptionKey).withEndpoint(endpoint);
    }


    public static void AnalyzeRemoteImage(ComputerVisionClient compVisClient) {
        /*
         * Analyze an image from a URL:
         *
         * Set a string variable equal to the path of a remote image.
         */
        String pathToRemoteImage = "https://github.com/Azure-Samples/cognitive-services-sample-data-files/raw/master/ComputerVision/Images/faces.jpg";

        // This list defines the features to be extracted from the image.
        List<VisualFeatureTypes> featuresToExtractFromRemoteImage = new ArrayList<>();
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.DESCRIPTION);
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.CATEGORIES);
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.TAGS);
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.FACES);
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.ADULT);
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.COLOR);
        featuresToExtractFromRemoteImage.add(VisualFeatureTypes.IMAGE_TYPE);

        System.out.println("\n\nAnalyzing an image from a URL ...");

        try {
            // Call the Computer Vision service and tell it to analyze the loaded image.
            ImageAnalysis analysis = compVisClient.computerVision().analyzeImage().withUrl(pathToRemoteImage)
                    .withVisualFeatures(featuresToExtractFromRemoteImage).execute();


            // Display image captions and confidence values.
            System.out.println("\nCaptions: ");
            for (ImageCaption caption : analysis.description().captions()) {
                    System.out.printf("\'%s\' with confidence %f\n", caption.text(), caption.confidence());
            }

            // Display image category names and confidence values.
            System.out.println("\nCategories: ");
            for (Category category : analysis.categories()) {
                    System.out.printf("\'%s\' with confidence %f\n", category.name(), category.score());
            }   

            // Display image tags and confidence values.
            System.out.println("\nTags: ");
            for (ImageTag tag : analysis.tags()) {
                    System.out.printf("\'%s\' with confidence %f\n", tag.name(), tag.confidence());
            }

            // Display any faces found in the image and their location.
            System.out.println("\nFaces: ");
            for (FaceDescription face : analysis.faces()) {
                    System.out.printf("\'%s\' of age %d at location (%d, %d), (%d, %d)\n", face.gender(), face.age(),
                    face.faceRectangle().left(), face.faceRectangle().top(),
                    face.faceRectangle().left() + face.faceRectangle().width(),
                    face.faceRectangle().top() + face.faceRectangle().height());
            }

            // Display whether any adult or racy content was detected and the confidence
            // values.
            System.out.println("\nAdult: ");
            System.out.printf("Is adult content: %b with confidence %f\n", analysis.adult().isAdultContent(),
            analysis.adult().adultScore());
            System.out.printf("Has racy content: %b with confidence %f\n", analysis.adult().isRacyContent(),
            analysis.adult().racyScore());

            // Display the image color scheme.
            System.out.println("\nColor scheme: ");
            System.out.println("Is black and white: " + analysis.color().isBWImg());
            System.out.println("Accent color: " + analysis.color().accentColor());
            System.out.println("Dominant background color: " + analysis.color().dominantColorBackground());
            System.out.println("Dominant foreground color: " + analysis.color().dominantColorForeground());
            System.out.println("Dominant colors: " + String.join(", ", analysis.color().dominantColors()));

            // Display any celebrities detected in the image and their locations.
            System.out.println("\nCelebrities: ");
            for (Category category : analysis.categories()) {
            if (category.detail() != null && category.detail().celebrities() != null) {
            for (CelebritiesModel celeb : category.detail().celebrities()) {
            System.out.printf("\'%s\' with confidence %f at location (%d, %d), (%d, %d)\n", celeb.name(),
                    celeb.confidence(), celeb.faceRectangle().left(), celeb.faceRectangle().top(),
                    celeb.faceRectangle().left() + celeb.faceRectangle().width(),
                    celeb.faceRectangle().top() + celeb.faceRectangle().height());
            }
            }
            }

            // Display any landmarks detected in the image and their locations.
            System.out.println("\nLandmarks: ");
            for (Category category : analysis.categories()) {
            if (category.detail() != null && category.detail().landmarks() != null) {
            for (LandmarksModel landmark : category.detail().landmarks()) {
            System.out.printf("\'%s\' with confidence %f\n", landmark.name(), landmark.confidence());
            }
            }
            }

            // Display what type of clip art or line drawing the image is.
            System.out.println("\nImage type:");
            System.out.println("Clip art type: " + analysis.imageType().clipArtType());
            System.out.println("Line drawing type: " + analysis.imageType().lineDrawingType());
            }

            catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            }
            }
            // END - Analyze an image from a URL.

            }