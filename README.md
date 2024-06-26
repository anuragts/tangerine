# 🍊 tangerine.
## in-memory vector database

tangerine is a in-memory vector database that is designed to have fast read and write speeds.

tangerine is not limited to vector database, as it has its own key-value store written from scratch in Java.

## Key-Value Store

### Installation and Running.

#### Using Docker

```bash
docker run -p 1111:1111 anuragdev123/tangerine-kv:latest
```

#### Manual Installation

Making sh executable
```bash
chmod +x tangerinekv-server.sh
chmod +x tangerinekv-cli.sh
chmod +x tangerinekv-compile.sh
```

> [!NOTE]
> The .java files are already compiled when running the scripts, but to prevent any issues with the classpath, you should compile them first.

```bash
./tangerinekv-compile.sh

```

Running the Tangerine-KV Server

```bash
 ./tangerinekv-server.sh
```

Running the Tangerine-KV Client
```bash
 ./tangerinekv-cli.sh
```
Testing 
```bash
$ SET key value
```
if server responds with `OK`, then the client & server is working.
 

### Commands

#### PING
```bash
$ PING
```

#### SET

```bash
$ SET key value
```

#### GET

```bash
$  GET key
```

#### REMOVE

```bash
$  REMOVE key
```

#### ALL

```bash
$  ALL
```

#### CONTAINS
```bash
$  CONTAINS key
```

#### CLEAR

```bash
$  CLEAR
```

#### EXPIRE

```bash
$  EXPIRE key seconds
```

#### TTL

```bash
$  TTL key
```

#### HELP

```bash
$  HELP
```

#### SUBSCRIBE
```bash
$  SUBSCRIBE topic
```

#### PUBLISH
```bash
$  PUBLISH topic message
```

<!-- #### UNSUBSCRIBE
```bash
$  UNSUBSCRIBE topic
``` -->


for progress and status check TODO.md

## License

MIT