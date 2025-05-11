import os
import time
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms
from cryptography.hazmat.backends import default_backend

# Function to encrypt random data
def chacha20_encrypt_random_data(key, nonce, total_size_bytes):
    cipher = Cipher(algorithms.ChaCha20(key, nonce), mode=None, backend=default_backend())
    encryptor = cipher.encryptor()

    chunk_size = 64 * 1024  # 64KB
    chunks = total_size_bytes // chunk_size

    start = time.process_time()

    for _ in range(chunks):
        random_chunk = os.urandom(chunk_size)  # Generate random 64KB chunk
        _ = encryptor.update(random_chunk)     # Encrypt it (we don't save the result)

    end = time.process_time()

    print(f"Encrypted {total_size_bytes / (1024**4):.2f} TB in {end - start:.2f} seconds")

# Main function
if __name__ == "__main__":
    key = b'K' * 32   # 32-byte key
    nonce = b'N' * 16 # 16-byte nonce
    terabytes = 1     # How many terabytes you want to encrypt

    total_size = terabytes * (1000 ** 3)  # Convert TB to bytes

    chacha20_encrypt_random_data(key, nonce, total_size)
