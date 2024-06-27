package com.dnghkm.high_school_community.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@ToString(exclude = {"user"})
@Builder
@Table(name = "verification_file")
public class VerificationFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "file_path")
    private String filePath;

    @NotNull
    @Column(name = "file_name_original")
    private String fileNameOriginal;

    @NotNull
    @Column(name = "file_name_UUID")
    private String fileNameUUID;

    @NotNull
    @Column(name = "file_type", length = 50)
    private String fileType; //MIME
}
