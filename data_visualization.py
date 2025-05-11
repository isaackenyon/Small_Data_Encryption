import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
import argparse
import os

def read_file_bytes(path):
    if not os.path.exists(path):
        raise FileNotFoundError(f"File not found: {path}")
    with open(path, 'rb') as f:
        return f.read()

def reshape_for_image(data):
    arr = np.frombuffer(data, dtype=np.uint8)
    size = int(np.ceil(np.sqrt(len(arr))))
    padded = np.pad(arr, (0, size**2 - len(arr)), 'constant')
    return padded.reshape((size, size))

def show_image(data, title):
    img = reshape_for_image(data)
    plt.imshow(img, cmap='gray')
    plt.title(title)
    plt.axis('off')
    plt.savefig("file_image.png")  # Save the image to a file instead of showing
    plt.close()  # Close the plot to avoid conflicts with subsequent plots

def visualize_difference(data1, data2):
    min_len = min(len(data1), len(data2))
    diff = np.abs(np.frombuffer(data1[:min_len], dtype=np.uint8).astype(int)
                - np.frombuffer(data2[:min_len], dtype=np.uint8).astype(int))

    size = int(np.ceil(np.sqrt(len(diff))))
    padded = np.pad(diff, (0, size**2 - len(diff)), 'constant')
    diff_img = padded.reshape((size, size))

    plt.figure(figsize=(6, 6))
    sns.heatmap(diff_img, cmap='viridis', cbar=True, square=True)
    plt.title("Byte-wise Absolute Difference Heatmap")
    plt.axis('off')
    plt.savefig("difference_heatmap.png")
    plt.close()

def visualize_xor(data1, data2):
    min_len = min(len(data1), len(data2))
    xor = np.bitwise_xor(np.frombuffer(data1[:min_len], dtype=np.uint8),
                         np.frombuffer(data2[:min_len], dtype=np.uint8))

    size = int(np.ceil(np.sqrt(len(xor))))
    padded = np.pad(xor, (0, size**2 - len(xor)), 'constant')
    xor_img = padded.reshape((size, size))

    plt.imshow(xor_img, cmap='plasma')
    plt.title("XOR Difference Visualization")
    plt.axis('off')
    plt.savefig("xor_visualization.png")
    plt.close()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Visualize differences between two binary files.")
    parser.add_argument("file1", help="Path to first binary file")
    parser.add_argument("file2", help="Path to second binary file")
    args = parser.parse_args()

    data1 = read_file_bytes(args.file1)
    data2 = read_file_bytes(args.file2)

    show_image(data1, "File 1 - Byte Data as Image")
    show_image(data2, "File 2 - Byte Data as Image")
    visualize_difference(data1, data2)
    visualize_xor(data1, data2)
