<div align="center">

# Namo Server

</div>

![banner](/docs/image/ios_header1_basic.jpg)

### 목차
-[🛠️ 나모는 이런 기술을 사용했어요](#-나모는-이런-기술을-사용했어요)
    - [For Code](#for-code)
    - [For Infra](#for-infra)
- [🔎 나모의 서버 구조](#-나모의-서버-구조)
    - [1️⃣ Service Code](#1-service-code)
    - [2️⃣ Infrastructure Architecture](#2-infrastructure-architecture)
    - [3️⃣ Multi-Module Architecture](#3-multi-module-architecture)
- [✍🏻 나모 서버 팀원들은 이렇게 작업해요](#-나모-서버-팀원들은-이렇게-작업해요)
    - [캐슬/이호성](#캐슬이호성)
    - [다나/서주원](#다나서주원)
    - [매실/김현재](#매실김현재)
    - [루카/최선규](#루카최선규)
    - [초코/김현지](#초코김현지)


## 🛠️ 나모는 이런 기술을 사용했어요

### For Code

<img src="https://img.shields.io/badge/Framework-555555?style=for-the-badge">![SpringBoot](https://img.shields.io/badge/springboot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)![spring_data_JPA](https://img.shields.io/badge/spring_data_JPA-%236DB33F?style=for-the-badge&logo=databricks&logoColor=white)![spring_security](https://img.shields.io/badge/spring_security-%236DB33F.svg?style=for-the-badge&logo=springsecurity&logoColor=white)

<img src="https://img.shields.io/badge/build-555555?style=for-the-badge">![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)

<img src="https://img.shields.io/badge/Test-555555?style=for-the-badge">![junit5](https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)![test_containers](https://img.shields.io/badge/test_containers-328ba3?style=for-the-badge&logo=reasonstudios&logoColor=white)![mockito](https://img.shields.io/badge/mockito-DA383E?style=for-the-badge&logo=mockito&logoColor=white)

<img src="https://img.shields.io/badge/Database-555555?style=for-the-badge">![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)![Flyway](https://img.shields.io/badge/Flyway-F7B500?style=for-the-badge&logo=flyway&logoColor=white)

### For Infra

<img src="https://img.shields.io/badge/Computing-555555?style=for-the-badge">![Amazon Ec2](https://img.shields.io/badge/amazon_ec2-FF9900.svg?style=for-the-badge&logo=amazonec2&logoColor=white)

<img src="https://img.shields.io/badge/CI/CD-555555?style=for-the-badge">![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)

<img src="https://img.shields.io/badge/Data_Storage-555555?style=for-the-badge">![Amazon S3](https://img.shields.io/badge/AWS_S3-569A31.svg?style=for-the-badge&logo=amazons3&logoColor=white)![Amazon RDS](https://img.shields.io/badge/amazon_RDS-527FFF.svg?style=for-the-badge&logo=amazonrds&logoColor=white)![Amazon ElastiCache](https://img.shields.io/badge/amazon_elasticache-C925D1.svg?style=for-the-badge&logo=amazonelasticache&logoColor=white)

<img src="https://img.shields.io/badge/Networking-555555?style=for-the-badge">![Route 53](https://img.shields.io/badge/amazon_rount_53-8C4FFF.svg?style=for-the-badge&logo=amazonroute53&logoColor=white)![Amazon ALB](https://img.shields.io/badge/amazon_alb-8C4FFF.svg?style=for-the-badge&logo=awselasticloadbalancing&logoColor=white)![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white)![CloudFront](https://img.shields.io/badge/Amazon_CloudFront-FF4E00?style=for-the-badge&logo=amazoncloudfront&logoColor=white)

<img src="https://img.shields.io/badge/Security-555555?style=for-the-badge">![AWS IAM](https://img.shields.io/badge/aws_iam-FF9900.svg?style=for-the-badge&logo=amazoniam&logoColor=white)![AWS Secrets Manager](https://img.shields.io/badge/aws_secrets_manager-DD344C?style=for-the-badge&logo=awssecretsmanager&logoColor=white)

<img src="https://img.shields.io/badge/Monitoring-555555?style=for-the-badge">![Amazon CloudWatch](https://img.shields.io/badge/amazon_cloudwatch-FF4F8B.svg?style=for-the-badge&logo=amazoncloudwatch&logoColor=white)

<img src="https://img.shields.io/badge/Serverless-555555?style=for-the-badge">![AWS Lambda](https://img.shields.io/badge/AWS_Lambda-FF9900?style=for-the-badge&logo=awslambda&logoColor=white)
## 🔎 나모의 서버 구조

### 1️⃣ Service Code

![System Architecture - Service-Logic](https://github.com/Namo-Mongmong/Server/assets/98688494/0ce16cb3-e2a7-45d9-afae-311c3b080a5b)

V1

[ Facade 패턴을 통한 클린 아키택쳐 구현을 노력했습니다.. ]

- 초록: 외부와 연결되는 레이어
- 빨강: 비지니스 로직을 다루는 레이어
- 노랑: 도메인 레이어


<br>

[ 참조의 방향은 항상 화살표 방향을 따릅니다. ]

- 코드의 의존성에 규칙을 두어 코드의 결합도가 올라가지 않도록합니다.
- 특히 Facade와 Converter의 사용을 통해 API 인터페이스와 내부 로직을 철저히 분리합니다.

v2

[ usecase와 ManageService를 도입했습니다. ]

<img width="297" alt="image" src="https://github.com/user-attachments/assets/98b9b7c4-843b-46ce-87c6-f4eb5d6ee6a0">

- 기존 Facade의 많은 의존성이 걸리며 유지보수가 힘들어져 새로운 방법을 도입했습니다.
- manageService가 인력사무소의 관리 소장과 같은 역할을 합니다.
- 각 Usecase에서 각 액션에 대한 행동들을 조합해 사용합니다.

Continue....
아직 완전한 클린 아키텍처가 되지 못해 한계를 느끼고 계속 더 나은 설계를 하기위해 노력하고 있습니다.



### 2️⃣ Infrastructure Architecture

![System Architecture - Infra](https://github.com/Namo-log/Server/assets/98688494/f10b18b7-44d2-4268-a201-c52f1f1a59c8)

### 3️⃣ Multi-Module Architecture

![System Architecture - Multi-Module](https://github.com/Namo-log/Server/assets/98688494/2f52032f-35c2-42cf-859f-f9a95e156cfe)

- [application](./application/README.md): 모듈은 외부에 제공되는 API와 관련된 기능을 제공한다.
- [clients](./clients/README.md): 모듈은 프로젝트 외부 서비스의 API를 호출하는 기능을 제공한다.
- [storage](./storage/README.md): 모듈은 데이터베이스와 관련된 기능을 제공한다.
- [core](./core/README.md): 다양한 모듈에서 공통적으로 사용되는 기능을 제공한다.
- [support](./support/README.md): 모듈은 프로젝트에 독립적으로 사용될 수 있는 기능을 제공한다.

## ✍🏻 나모 서버 팀원들은 이렇게 작업해요

### 캐슬/이호성

- [여러 이미지가 포함된 데이터를 수정하기 : 나모의 이미지 기능 수정 과정 1편](https://namo-log.vercel.app/server-editing-image)
- [Presigned URL 도입기 : 나모의 이미지 기능 수정 과정 2편](https://namo-log.vercel.app/server-editing-image-2)
- [AWS Lambda를 활용한 서버리스 이미지 리사이징 도입기 : 나모의 이미기 기능 수정 과정 3편](https://namo-log.vercel.app/server-editing-image-3)
- [CloudFront를 활용한 CDN 도입 : 나모의 이미지 기능 수정과정 4편](https://namo-log.vercel.app/server-editing-image-4)
- [객체지향적인 나모 만들기 : 역할과 책임의 재분배](https://tech.namong.shop/object-oriented-namo)
- [더 빠른 모임 기록 제공하기 : Redis 활용기](https://namo-log.vercel.app/faster-faster-cache)
- [중단 없는 이미지 제공하기 : Redis 활용기 2탄](https://namo-log.vercel.app/uninterrupted-image-delivery)

### 다나/서주원

- [나모 DB의 더 나은 미래를 위한 Flyway 도입기](https://namo-log.vercel.app/server-using-flyway)

### 매실/김현재

- [동시성 이슈 해결하기 - 낙관적 락](https://namo-log.vercel.app/server-synchronized-db)

### 루카/최선규

- [우당탕탕 AWS 서버 이전기](https://namo-log.vercel.app/server-transfer-on-aws)
- [SSM으로 보안은 강화하고 관리는 쉽게하자](https://namo-log.vercel.app/bastion-host-to-ssm)

### 초코/김현지

- [애플 로그인 & 회원탈퇴 구현하기(1)](https://namo-log.vercel.app/server-apple-1)
