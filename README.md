<div align="center">

# Namo Server
  
</div>

![banner](/docs/image/ios_header1_basic.jpg)


### 목차
- [🛠️ 나모는 이런 기술을 사용했어요](#-나모는-이런-기술을-사용했어요)
  - [For Code](#For-Code)
  - [For Infra](#For-Infra)
- [🔎 나모의 서버 구조](#-나모의-서버-구조)
  - [Service Code](#Service-Code)
  - [Service Infra](#Service-Infra)
- [✍🏻 나모 서버 팀원들은 이렇게 작업해요](#-나모-서버-팀원들은-이렇게-작업해요)


## 🛠️ 나모는 이런 기술을 사용했어요

### For Code

<img src="https://img.shields.io/badge/Framework-555555?style=for-the-badge">![SpringBoot](https://img.shields.io/badge/springboot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)![spring_data_JPA](https://img.shields.io/badge/spring_data_JPA-%236DB33F?style=for-the-badge&logo=databricks&logoColor=white)![spring_security](https://img.shields.io/badge/spring_security-%236DB33F.svg?style=for-the-badge&logo=springsecurity&logoColor=white)

<img src="https://img.shields.io/badge/build-555555?style=for-the-badge">![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)

<img src="https://img.shields.io/badge/Test-555555?style=for-the-badge">![junit5](https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)![test_containers](https://img.shields.io/badge/test_containers-328ba3?style=for-the-badge&logo=reasonstudios&logoColor=white)![mockito](https://img.shields.io/badge/mockito-DA383E?style=for-the-badge&logo=mockito&logoColor=white)

<img src="https://img.shields.io/badge/Database-555555?style=for-the-badge">![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)


### For Infra

<img src="https://img.shields.io/badge/Computing-555555?style=for-the-badge">![Amazon Ec2](https://img.shields.io/badge/amazon_ec2-FF9900.svg?style=for-the-badge&logo=amazonec2&logoColor=white)

<img src="https://img.shields.io/badge/CI/CD-555555?style=for-the-badge">![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)

<img src="https://img.shields.io/badge/Data_Storage-555555?style=for-the-badge">![Amazon S3](https://img.shields.io/badge/AWS_S3-569A31.svg?style=for-the-badge&logo=amazons3&logoColor=white)![Amazon RDS](https://img.shields.io/badge/amazon_RDS-527FFF.svg?style=for-the-badge&logo=amazonrds&logoColor=white)![Amazon ElastiCache](https://img.shields.io/badge/amazon_elasticache-C925D1.svg?style=for-the-badge&logo=amazonelasticache&logoColor=white)

<img src="https://img.shields.io/badge/Networking-555555?style=for-the-badge">![Route 53](https://img.shields.io/badge/amazon_rount_53-8C4FFF.svg?style=for-the-badge&logo=amazonroute53&logoColor=white)![Amazon ALB](https://img.shields.io/badge/amazon_alb-8C4FFF.svg?style=for-the-badge&logo=awselasticloadbalancing&logoColor=white)![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white)

<img src="https://img.shields.io/badge/Security-555555?style=for-the-badge">![AWS IAM](https://img.shields.io/badge/aws_iam-FF9900.svg?style=for-the-badge&logo=amazoniam&logoColor=white)![AWS Secrets Manager](https://img.shields.io/badge/aws_secrets_manager-DD344C?style=for-the-badge&logo=awssecretsmanager&logoColor=white)

<img src="https://img.shields.io/badge/Monitoring-555555?style=for-the-badge">![Amazon CloudWatch](https://img.shields.io/badge/amazon_cloudwatch-FF4F8B.svg?style=for-the-badge&logo=amazoncloudwatch&logoColor=white)


## 🔎 나모의 서버 구조

### Service Code

![System Architecture - Namo Server (1)](https://github.com/Namo-Mongmong/Server/assets/98688494/0ce16cb3-e2a7-45d9-afae-311c3b080a5b)

[ Facade 패턴을 통해 틀린 아키택쳐를 구현하고자 노력합니다. ]
- 초록: 외부와 연결되는 레이어
- 빨강: 비지니스 로직을 다루는 레이어
- 노랑: 도메인 레이어

<br>

[ 참조의 방향은 항상 화살표 방향을 따릅니다. ]
- 코드의 의존성에 규칙을 두어 코드의 결합도가 올라가지 않도록합니다.
- 특히 Facade와 Converter의 사용을 통해 API 인터페이스와 내부 로직을 철저히 분리합니다.


### Service Infra

![System Architecture - Namo Server](https://github.com/Namo-Mongmong/Server/assets/98688494/3dea824a-c173-4bc6-9ae6-ba135eef8165)



## ✍🏻 나모 서버 팀원들은 이렇게 작업해요

### 매실/김현재

- [동시성 이슈 해결하기 - 낙관적 락](https://namo-log.vercel.app/server-synchronized-db)

### 루카/최선규

- [우당탕탕 AWS 서버 이전기](https://namo-log.vercel.app/server-transfer-on-aws)
- [SSM으로 보안은 강화하고 관리는 쉽게하자](https://namo-log.vercel.app/bastion-host-to-ssm)

### 초코/김현지

- [애플 로그인 & 회원탈퇴 구현하기(1)](https://namo-log.vercel.app/server-apple-1)
