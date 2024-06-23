# Application 모듈

- [Global Rule](#global-rule)
- [DB-Mysql Module](#db-mysql-module)
    - [제공되는 기능](#제공되는-기능)
- [Contributors](#contributors)
- [Update History](#update-history)

## Global Rule

- `application` 모듈은 프로젝트의 Application Layer에 해당하는 기능을 제공한다.
    - Application Layer는 Business Logic을 구현하는 레이어이다.
    - Application Layer는 Presentation Layer와 Domain Layer를 연결하는 역활을 한다.
- `application` 모듈은 다른 모듈에 의존성을 가질 수 있다.

## External-API Module

- `:application:external-api` 모듈은 외부에 제공되는 API와 관련된 기능을 제공한다.

### 제공되는 기능

- API Controller
- Business Logic
    - Business Service
    - Business Facade
- Response/Request DTO
- Swagger Documentation

## Contributors

| ![루카/최선규](https://avatars.githubusercontent.com/u/98688494?v=4) | ![매실/김현재](https://avatars.githubusercontent.com/u/41482946?v=4) | ![초코/김현지](https://avatars.githubusercontent.com/u/112065014?v=4) |
|:---------------------------------------------------------------:|:---------------------------------------------------------------:|:----------------------------------------------------------------:|
|              [루카/최선규](https://github.com/luke0408)              |               [매실/김현재](https://github.com/galug)                |              [초코/김현지](https://github.com/hyeonji91)              |

## Update History

- 2024.06.11
    - `application` 모듈 및 `external-api` 모듈 생성
- 2024.06.18
    - `jpa` 관련 설정 이관 (`db-mysql` 모듈로 이동)
    - `user` 도메인 모듈 분리 (`entity`, `repository` 패키지 `db-mysql` 모듈로 이동)
- 2024.06.20
    - `individual` 도메인 모듈 분리 (`entity`, `repository` 패키지 `db-mysql` 모듈로 이동)
- 2024.06.23
    - `group` 도메인 모듈 분리 (`entity`, `repository` 패키지 `db-mysql` 모듈로 이동)
