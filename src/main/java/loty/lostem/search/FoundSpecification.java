package loty.lostem.search;

import jakarta.persistence.criteria.CriteriaBuilder;
import loty.lostem.entity.PostFound;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class FoundSpecification {
    public static Specification<PostFound> likeTitle(String title) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("title"),"%" + title + "%");
    }
    public static Specification<PostFound> equalCategory(String category) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("category"), category);
    }
    public static Specification<PostFound> equalDate(LocalDateTime date) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("date"), date);
    }
    public static Specification<PostFound> equalArea(String area) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("area"), area);
    }
    public static Specification<PostFound> likePlace(String place) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("place"), "%" + place + "%");
    }
    public static Specification<PostFound> equalItem(String item) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("item"), item);
    }
    public static Specification<PostFound> likeContents(String contents) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("contents"), "%" + contents + "%");
    }
    public static Specification<PostFound> equalState(String state) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("state"), state);
    }
    public static Specification<PostFound> likeStorage(String storage) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("storage"), "%" + storage +"%");
    }
}