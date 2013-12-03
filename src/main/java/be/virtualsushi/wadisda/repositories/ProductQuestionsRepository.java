package be.virtualsushi.wadisda.repositories;

import org.springframework.stereotype.Repository;

import be.virtualsushi.wadisda.entities.ProductQuestion;

@Repository(value = "productQuestions")
public interface ProductQuestionsRepository extends BaseEntityRepository<ProductQuestion> {

}
