package cn.keking.entity.dao;


import cn.keking.entity.model.PreviewAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Dependency Spring Data JPA Integration Hibernate
 * @author yanting
 * @date 2018/11/8
 */
@Repository
public interface PreviewAttachRepository extends JpaRepository<PreviewAttachment, Integer> {

    /**
     * @param id
     * @return
     */
    PreviewAttachment findById(Long id);

    /**
     * @param previewAttachment
     * @return
     */
    @Override
    PreviewAttachment save(PreviewAttachment previewAttachment);

    /**
     * @param previewName
     * @return
     */
    @Query(value = "SELECT p FROM PreviewAttachment p WHERE previewName=:previewName")
    PreviewAttachment findPreviewAttach(@Param("previewName") String previewName);

}