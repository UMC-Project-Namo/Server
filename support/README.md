# Support 모듈

- [Global Rule](#global-rule)
- [Logging Module](#logging-module)
    - [제공되는 기능](#제공되는-기능)
- [Contributors](#contributors)
- [Update History](#update-history)

## Global Rule

- `support` 모듈은 프로젝트에 독립적으로 사용될 수 있는 기능을 제공한다.
    - 각 `support` 모듈은 독립적인 책임과 역활을 가진다.
    - 각 `support` 타 프로젝트에서도 사용될 수 있도록 설계되어야 한다.
- `support` 모듈은 다른 모듈에 의존성을 갖지 않아야 한다.

## Logging Module

- `:support:logging` 모듈은 로깅과 관련된 기능을 제공한다.

### 제공되는 기능

- Logback Configuration
- Logback Template

## Contributors

| ![루카/최선규](https://avatars.githubusercontent.com/u/98688494?v=4) | 
|:---------------------------------------------------------------:|
|              [루카/최선규](https://github.com/luke0408)              |

## Update History

- 2024.06.11: `spuport` 모듈 및 `logging` 모듈 생성
