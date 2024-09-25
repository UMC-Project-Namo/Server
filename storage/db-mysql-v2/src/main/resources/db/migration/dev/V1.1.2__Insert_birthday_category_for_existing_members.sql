-- 기존 3번 카테고리에 대하여 4번으로 수정
UPDATE category
SET
    order_number = 4,
    updated_at = CURRENT_TIMESTAMP
WHERE
        order_number = 3;

-- Member에 대해 생일 기본 카테고리 생성
INSERT INTO category (
    member_id,
    palette_id,
    name,
    type,
    order_number,
    is_shared,
    status,
    created_at,
    updated_at
)
SELECT
    m.id AS member_id,
    3,
    '생일',
    '4',
    3,
    true AS is_shared,
    1,
    CURRENT_TIMESTAMP AS created_at,
    CURRENT_TIMESTAMP AS updated_at
FROM
    member m;
