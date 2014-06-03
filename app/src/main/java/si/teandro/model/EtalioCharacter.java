package si.teandro.model;

import si.teandro.model.EtalioAttribute;

/**
 * Created by jernej on 3.6.14.
 */
public class EtalioCharacter {

    private EtalioAttribute bodyPaint;
    private EtalioAttribute attribute;
    private EtalioAttribute body;

    public EtalioAttribute getBody() {
        return body;
    }

    public void setBody(EtalioAttribute body) {
        this.body = body;
    }

    public EtalioAttribute getBodyPaint() {
        return bodyPaint;
    }

    public void setBodyPaint(EtalioAttribute bodyPaint) {
        this.bodyPaint = bodyPaint;
    }

    public EtalioAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(EtalioAttribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        return "EtalioCharacter{" +
                "bodyPaint=" + bodyPaint +
                ", attribute=" + attribute +
                ", body=" + body +
                '}';
    }
}
