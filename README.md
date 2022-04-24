# Advanced_EncDec App
##Introduction
The software uses a combination of optimized RSA and DES algorithm to encrypt/decrypt a file (of any type). Provides a platform for users to share secured files without worrying of threats or data leaks.

##Background
Data security is the need of the hour in today's tech world. RSA is the most Secure Encryption Algorithm. But, RSA algorithm is a slow asymmetric algorithm. 10 times slower than DES. Whereas, DES has the drawback of sharing secret key.

##Improvised Solution
The software uses RSA to encrypt the private key and applies round encryption to the data (with round specific keys generated from user private key with imrovised FISHER-YATES Random Shuffle). User can share the key over any unreliable medium. On Decryption side User needs provides the RSA-Encrypted key (which is Decrypted) and data is decrypted.
