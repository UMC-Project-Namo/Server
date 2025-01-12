-- Point 테이블 생성
CREATE TABLE point (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       member_id BIGINT NOT NULL,
                       balance BIGINT NOT NULL DEFAULT 0,
                       CONSTRAINT fk_point_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
);

-- PointTransaction 테이블 생성
CREATE TABLE point_transaction (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   point_id BIGINT NOT NULL,
                                   transaction_type VARCHAR(20) NOT NULL,
                                   transaction_status VARCHAR(20) NOT NULL,
                                   amount BIGINT NOT NULL,
                                   transaction_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   description VARCHAR(255),
                                   CONSTRAINT fk_point_transaction_point FOREIGN KEY (point_id) REFERENCES point (id) ON DELETE CASCADE
);
