package cn.keking.entity.model;

import cn.keking.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yanting
 * @date 2018/11/8
 */
@Entity
@Table(name = "DP_Preview_Attachment")
public class PreviewAttachment extends BaseEntity {
    @Column(name = "previewName")
    private String previewName;
    @Column(name = "previewAttach_ID")
    private String previewAttachID;
    @Column(name = "fileLocation")
    private String fileLocation;

    public String getPreviewName() {
        return previewName;
    }

    public void setPreviewName(String previewName) {
        this.previewName = previewName;
    }

    public String getPreviewAttachID() {
        return previewAttachID;
    }

    public void setPreviewAttachID(String previewAttachID) {
        this.previewAttachID = previewAttachID;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
}
