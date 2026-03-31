#!/usr/bin/env python3
"""
Simple PDF to text extractor using PyPDF2.
Usage: python tools/read_pdf_text.py path/to/file.pdf [--pages N-M] [--out output.txt]
"""
import argparse
from pathlib import Path

try:
    from PyPDF2 import PdfReader
except Exception as e:
    PdfReader = None
    # Defer error until runtime if library missing

def extract_text(pdf_path, pages=None):
    if PdfReader is None:
        raise RuntimeError("PyPDF2 is not installed. Run: pip install PyPDF2")
    reader = PdfReader(str(pdf_path))
    total = len(reader.pages)
    if pages:
        start, end = pages
        start = max(1, min(start, total))
        end = max(start, min(end, total))
        parts = []
        for idx in range(start-1, end):
            try:
                parts.append(reader.pages[idx].extract_text() or "")
            except Exception:
                parts.append("")
        return "\n".join(parts)
    else:
        parts = []
        for p in reader.pages:
            parts.append(p.extract_text() or "")
        return "\n".join(parts)

def parse_pages(val):
    if not val:
        return None
    if "-" in val:
        a, b = val.split("-", 1)
        return int(a), int(b)
    return int(val), int(val)

def main():
    parser = argparse.ArgumentParser(description="PDF to text extractor (PyPDF2).")
    parser.add_argument("path", type=Path, help="PDF file path")
    parser.add_argument("--pages", type=str, help="Page range like 1-3", default=None)
    parser.add_argument("--out", type=str, help="Output text file", default=None)
    args = parser.parse_args()

    pages = parse_pages(args.pages) if args.pages else None
    text = extract_text(args.path, pages)
    if args.out:
        Path(args.out).write_text(text, encoding="utf-8")
    else:
        print(text)

if __name__ == "__main__":
    main()
