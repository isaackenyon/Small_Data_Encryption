import time
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms
from cryptography.hazmat.backends import default_backend

# Function to encrypt the file
def chacha20_encrypt_file(key, nonce, input_file_path, output_file_path):
    cipher = Cipher(algorithms.ChaCha20(key, nonce), mode=None, backend=default_backend())
    encryptor = cipher.encryptor()

    start = time.process_time()

    with open(input_file_path, 'rb') as f_in, open(output_file_path, 'wb') as f_out:
        while chunk := f_in.read(64 * 1024):  # 64KB chunks
            f_out.write(encryptor.update(chunk))

    end = time.process_time()
    print(f"Encryption took {end - start:.6f} seconds")

# Function to decrypt the file
def chacha20_decrypt_file(key, nonce, input_file_path, output_file_path):
    with open(input_file_path, 'rb') as f_in:
        _ = f_in.read()  # Just to ensure file is read and not cached

    cipher = Cipher(algorithms.ChaCha20(key, nonce), mode=None, backend=default_backend())
    decryptor = cipher.decryptor()

    start = time.process_time()

    with open(input_file_path, 'rb') as f_in, open(output_file_path, 'wb') as f_out:
        while chunk := f_in.read(64 * 1024):
            f_out.write(decryptor.update(chunk))

    end = time.process_time()
    print(f"Decryption took {end - start:.6f} seconds")

# Main function
if __name__ == "__main__":
    mode = "encrypt"  # Change to "decrypt" for decryption
    input_file = '../Data_Levels/1_GB.bin'
    output_file = '../OUTPUT.bin'
    key = b'K' * 32   # 32-byte key
    nonce = b'N' * 16 # 16-byte nonce

    if mode == "encrypt":
        chacha20_encrypt_file(key, nonce, input_file, output_file)
    elif mode == "decrypt":
        chacha20_decrypt_file(key, nonce, input_file, output_file)
