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

JAR (v0.1): https://www.dropbox.com/s/sziba30jejx8h60/PanicFiles%200.1%20alpha.jar?dl=0
JAR (v0.1b): https://www.dropbox.com/s/p0thgsmgfyzcnr8/PanicFiles%200.1b%20alpha.jar?dl=0
EXE (v0.1b): https://www.dropbox.com/s/a26unp11u4xaj55/PanicFiles%200.1b.exe?dl=0

Version with interface. Launch jar and load file list.

