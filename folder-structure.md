# ClinicSys Folder Structure

This document describes the recommended folder and file structure for all microservices and infrastructure components in the ClinicSys platform.

---

## Monorepo Root

```
ClinicSys/
  docker-compose.yml
  README.md
  system-design.md
  folder-structure.md
  .env
  scripts/
    init-db.sql
  common-lib/
    pom.xml
    src/
      main/java/com/clinicsys/common/
      test/java/com/clinicsys/common/
  api-gateway/
    pom.xml
    src/
      main/java/com/clinicsys/apigateway/
      main/resources/
      test/java/com/clinicsys/apigateway/
  service-registry/
    pom.xml
    src/
      main/java/com/clinicsys/serviceregistry/
      main/resources/
      test/java/com/clinicsys/serviceregistry/
  config-server/
    pom.xml
    src/
      main/java/com/clinicsys/configserver/
      main/resources/
      test/java/com/clinicsys/configserver/
```

---

## Microservice Template

Each business microservice follows this structure (replace `<service-name>`):

```
<service-name>/
  pom.xml
  src/
    main/java/com/clinicsys/<service-name>/
      controller/
      service/
      repository/
      model/
      dto/
      event/
      config/
      exception/
      util/
    main/resources/
      application.yml
      logback.xml
    test/java/com/clinicsys/<service-name>/
      controller/
      service/
      repository/
      ...
```

---

## All Services

```
ClinicSys/
  auth-service/
    ... (see template above)
  user-service/
    ...
  clinic-service/
    ...
  package-service/
    ...
  reservation-service/
    ...
  schedule-service/
    ...
  payment-service/
    ...
  notification-service/
    ...
  promo-service/
    ...
  staff-service/
    ...
  reporting-service/
    ...
  inventory-service/
    ...
  medicalhistory-service/
    ...
  feedback-service/
    ...
  wallet-service/
    ...
  referral-service/
    ...
  audit-service/
    ...
```

---

## Example: reservation-service

```
reservation-service/
  pom.xml
  src/
    main/java/com/clinicsys/reservation/
      controller/
      service/
      repository/
      model/
      dto/
      event/
      config/
      exception/
      util/
    main/resources/
      application.yml
      logback.xml
    test/java/com/clinicsys/reservation/
      controller/
      service/
      repository/
      ...
```

---

## Notes
- Each service has its own `pom.xml` and is a standalone Spring Boot Maven project.
- `common-lib` is a shared library for DTOs, exceptions, and utilities.
- `scripts/` contains DB initialization and utility scripts.
- `docker-compose.yml` orchestrates all services, databases, Kafka, RabbitMQ, Redis, etc.
- Add additional folders (e.g., `storage/`, `migration/`) as needed for file storage or DB migrations. 