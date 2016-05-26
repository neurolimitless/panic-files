# PanicFiles
Application for encryption files

Currently only AES_CFB implemented.
For launch compile sources or install via Maven and execute Main with next args:
- java Main key initVector mode path cipher

Arguments:
* key - must be 16 char (any string)
* initVector - must be 16 char (any string)
* mode - 1 for encryption \ 2 for decryption
* path - path to directory
* cipher - AES_CFB

* java Main abc123def456ghk7 superINITvector1 1 C:\filesToEncryption\ AES_CFB



