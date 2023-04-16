# Kafka Streams Pipeline

This streams pipeline application processes the messages from a topic of Apache Kafka into Mongo DB as ETL.

```mermaid
graph LR;
    AK([Apache Kafka])
    subgraph Transform
    PA(Streams Pipeline App)
    end
    MD[(Mongo DB)]
    AK--Extract-->PA
    PA--Load-->MD
```

## Requirements

- Java 17
- Apache Kafka
- Mongo DB

## Usage Quick Start

### 1. Build

Mac or Linux

```bash
./gradlew assemble
```

### 2. Run

Mac or Linux

```bash
java -jar ./app/build/libs/kafka-streams-pipeline-1.0.0-SNAPSHOT-all.jar
```
