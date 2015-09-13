package autoMaintProgram;

import org.springframework.data.annotation.Id;

public class AccidentEntity {

    @Id
    private String id;

    private String type;
    private String description;
    private String damageLevel;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDamageLevel(String damageLevel) {
        this.damageLevel = damageLevel;
    }

    public String getDamageLevel() {
        return damageLevel;
    }
}
