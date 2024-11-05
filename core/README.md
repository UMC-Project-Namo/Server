# Core 모듈

- [Global Rule](#global-rule)
- [Core-Common Module](#core-common-module)
    - [제공되는 기능](#제공되는-기능)
- [Contributors](#contributors)
- [Update History](#update-history)

## Global Rule

- `core` 모듈은 다수의 모듈에서 공통적으로 사용되는 기능을 제공한다.
    - 기능 제공의 범위에 따라 `core` 모듈을 세분화하여 구성할 수 있다.
- Type, Interface, Enum, Decorator, Utility 등의 기능을 제공한다.
- `core` 모듈은 다른 모듈에서 사용되는 기능을 제공하기 위한 목적으로만 사용되어야 한다.
    - `core` 모듈은 다른 모듈에 의존성을 갖지 않아야 한다.
    - `core` 모듈은 다른 모듈에 의존성을 갖는 것을 허용하지 않는다.
- `core` 모듈의 수정은 최소화 되어야 한다.
    - `core` 모듈의 수정은 다른 모듈에 영향을 미치지 않아야 한다.
    - `core` 모듈은 가능한 작게 유지되어야 한다.

## Core-Common Module

- `:core:core-common` 모듈은 다수의 모듈에서 공통적으로 사용되는 기능을 제공한다.

### 제공되는 기능

- Exception Handling
- API Response Template
- API Response Status(enum type)

## Core-Infra Module

- `:core:core-infra` 모듈은 다수의 모듈에서 사용될 수 있는 인프라 설정 및 기능을 제공한다.

### 제공되는 기능

- JwtProvider, JwtClaims

## Contributors

| ![루카/최선규](https://avatars.githubusercontent.com/u/98688494?v=4) | ![매실/김현재](https://avatars.githubusercontent.com/u/41482946?v=4) | ![초코/김현지](https://avatars.githubusercontent.com/u/112065014?v=4) | ![캐슬/이호성](https://avatars.githubusercontent.com/u/62132755?v=4) | ![다나/서주원](https://avatars.githubusercontent.com/u/85955988?v=4) |
|:---------------------------------------------------------------:|:---------------------------------------------------------------:|:----------------------------------------------------------------:|:---------------------------------------------------------------:|:---------------------------------------------------------------:|
| [루카/최선규](https://github.com/luke0408) | [매실/김현재](https://github.com/galug) | [초코/김현지](https://github.com/hyeonji91) | [캐슬/이호성](https://github.com/hosunglee222) | [다나/서주원](https://github.com/joowojr) |

