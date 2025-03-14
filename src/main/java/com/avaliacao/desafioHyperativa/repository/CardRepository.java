package com.avaliacao.desafioHyperativa.repository;

import com.avaliacao.desafioHyperativa.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByNumber(String number);

    Optional<Card> findByCardKey(String key);

    List<Card> findByUserId(Long userId);

    Card findByCardKeyAndUserId(String cardKey, long userId);

}