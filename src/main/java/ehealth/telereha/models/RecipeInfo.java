package ehealth.telereha.models;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "RecipeInfo")
public class RecipeInfo {
    @Id
    public String id;

    public Binary RecipeFileContent;
    public String FileName;
    public String Email;

    // Default constructor
    public RecipeInfo() {
    }

    // Constructor with parameters
    public RecipeInfo(Binary RecipeFileContent, String FileName, String Email) {
        this.RecipeFileContent = RecipeFileContent;
        this.FileName = FileName;
        this.Email = Email;
    }

    // Getters and Setters (optional but recommended for proper encapsulation)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Binary getRecipeFileContent() {
        return RecipeFileContent;
    }

    public void setRecipeFileContent(Binary recipeFileContent) {
        RecipeFileContent = recipeFileContent;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
