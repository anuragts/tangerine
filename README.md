# 🍊 tangerine.
## in-memory vector database

## Running

Making sh executable
```bash
chmod +x tangerinekv-compile.sh 
chmod +x tangerinekv-server.sh
chmod +x tangerinekv-cli.sh
```

> [!NOTE]
> The .java files are already compiled, but to prevent any issues with the classpath, you should compile them first.

```bash
$ ./tangerinekv-compile.sh
```

```bash
$ ./tangerinekv-server.sh
```

```bash
$ ./tangerinekv-cli.sh
```

## Commands
### set

```bash
$ set key value
```

### get

```bash
$  get key
```

### remove

```bash
$  remove key
```

### see all

```bash
$  see all
```

### quit

```bash
$ quit
```

## License

MIT