# Storage 모듈

- [Global Rule](#global-rule)
- [DB-Mysql Module](#db-mysql-module)
    - [제공되는 기능](#제공되는-기능)
    - [Flyway Migration Guideline](#Flyway Migration Guideline)
- [Contributors](#contributors)
- [Update History](#update-history)

## Global Rule

- `storage` 모듈은 DB와 관련된 기능을 제공한다.
    - DB의 종류에 따라 `storage` 모듈을 세분화하여 구성할 수 있다.
- `storage` 모듈은 Business 로직을 알지 못해야 한다.
    - `storage` 모듈은 Business 로직에 의존성을 갖지 않아야 한다.
    - `storage` 모듈의 수정이 Business 로직에 영향을 미치지 않아야 한다.
- `Entity`와 같은 도메인 개념이 아닌 개념은 `storage` 모듈 내부에서만 사용되어야 한다.
    - `Entity`, `Embeddable`, `Value Object` 등의 도메인 개념은 `storage` 모듈 외부에서도 사용될 수 있다.
    - 그 외의 QueryDSL을 위한 `DTO Class` 등은 `storage` 모듈 내부에서만 사용되어야 한다.

## DB-Mysql Module

- `:storage:db-mysql` 모듈은 MySQL DB와 관련된 기능을 제공한다.

### 제공되는 기능

- Domain Service
    - Transaction
- Repository
    - QueryDSL
    - JPA
- Entity

### Flyway Migration Guideline

- Migration 파일 명명 규칙
  > V[Version]__[Description].sql
    - Version : 버전정보
        - 1: 메이저 버전
        - 0: 마이너 버전
        - 0: 패치 버전
    - Separator : 언더바 **2개** __
    - Description : 파일 설명
        - Create_[테이블명]_table : 테이블 생성
        - Drop_[테이블명]_table : 테이블 삭제
        - Add_[컬럼명]_column_to_[테이블명]_table : 테이블 컬럼 추가
        - Drop_[컬럼명]_column_from_[테이블명]_table : 테이블 컬럼 삭제
        - Refactor_[테이블명]_table: 테이블 리팩토링
        - Add_foreign_key_to_[테이블명]_table

- 파일명 예시
    - **`V1.0.0__Initial_schema.sql`** : 스키마 초기 설정
    - **`V1.0.1__Add_age_column_to_user_table.sql`**
    - **`V1.1.0__Drop_address_column_from_user_table.sql`**
    - **`V2.0.0__Refactor_user_table.sql`**

- 유의사항
    - migration 파일은 `:storage:db-mysql:src:main:resources:migration`에 위치한다.
    - mysql.yml의 flyway.baseline-version 속성은 **해당 버전 이후**의 마이그레이션만 적용하는 속성이다.
    - 이미 적용된 migration 파일은 수정하지 않는다.
    - 새로운 변경사항은 항상 새 migration 파일로 작성한다.

## DB-Redis Module

- `:storage:db-redis` 모듈은 Redis DB와 관련된 기능을 제공한다.

### 제공되는 기능

- Data Cache

## Contributors

| ![루카/최선규](https://avatars.githubusercontent.com/u/98688494?v=4) | ![매실/김현재](https://avatars.githubusercontent.com/u/41482946?v=4) | ![초코/김현지](https://avatars.githubusercontent.com/u/112065014?v=4) |
|:---------------------------------------------------------------:|:---------------------------------------------------------------:|:----------------------------------------------------------------:|
|              [루카/최선규](https://github.com/luke0408)              |               [매실/김현재](https://github.com/galug)                |              [초코/김현지](https://github.com/hyeonji91)              |

## Update History

- 2024.06.11
    - `core` 모듈 및 `core-common` 모듈 생성
- 2024.06.18
    - `jpa` 관련 설정 이관 (`external-api` 모듈에서 이동)
    - `user` 도메인 모듈 분리 (`entity`, `repository` 패키지 `external-api` 모듈에서 이동)
- 2024.06.20
    - `individual` 도메인 모듈 분리 (`entity`, `repository` 패키지 `external-api` 모듈에서 이동)
- 2024.06.24
    - `group` 도메인 모듈 분리 (`entity`, `repository` 패키지 `external-api` 모듈에서 이동)
