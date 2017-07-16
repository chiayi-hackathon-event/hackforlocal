package forms;

/**
 * Created by Saber on 2015/11/19.
 */
public class formNewModel {
    protected long modelID;
    protected String modelName;
    protected String description;
    protected String scenario;
    protected String instruction;
    protected String member_token;
    protected String modelTags;
    protected int category;
    protected int license;
    protected String stl;
    protected String image;

    public void setModelID(Long modelID) {
        this.modelID = modelID;
    }

    public Long getModelID() {
        return modelID;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getScenario() {
        return scenario;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setMember_token(String member_token) {
        this.member_token = member_token;
    }

    public String getMember_token() {
        return member_token;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getCategory() {
        return category;
    }

    public void setLicense(int license) {
        this.license = license;
    }

    public int getLicense() {
        return license;
    }

    public void setStl(String stl) {
        this.stl = stl;
    }

    public String getStl() {
        return stl;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setModelTags(String tags){this.modelTags = tags;}

    public String getModelTags(){return this.modelTags;}
}
