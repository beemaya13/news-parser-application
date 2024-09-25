package com.nilga.shared.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a news article. Maps to the 'news' table in MySQL.
 */
@Entity
@Table(name = "news")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String headline;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "publication_time", nullable = false)
    private LocalDateTime publicationTime;
}