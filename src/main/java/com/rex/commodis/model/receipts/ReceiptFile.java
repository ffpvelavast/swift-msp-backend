/** JPA entity representing the ReceiptFile table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "receipt_files", schema = "receipts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptFile {

    @Id
    @SequenceGenerator(
            name = "GEN_ReceiptFile",
            sequenceName = "receipt_files_id_seq",
            schema = "receipts",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_ReceiptFile")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "receipt_id", nullable = false)
    private Long receiptId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", insertable = false, updatable = false)
    private Receipt receipt;

    @Column(name = "receiptfileid", nullable = false)
    private UUID receiptFileId;

    @Column(name = "receiptnumber_seq", nullable = false)
    private Integer receiptNumberSeq;

    @Column(name = "filetype_id", nullable = false)
    private Short fileTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filetype_id", insertable = false, updatable = false)
    private Filetypes fileType;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "mediatype", nullable = false)
    private String mediaType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}