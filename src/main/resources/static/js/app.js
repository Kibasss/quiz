// ===== 1. Fungsi hitung (hasil dihitung di halaman, tidak disimpan ke database) =====
function formatAngka(n) {
  if (!Number.isFinite(n)) return "-";
  // dibulatkan 8 angka di belakang koma supaya 0.1 + 0.2 tampil 0.3
  return String(parseFloat(n.toFixed(8)));
}

function hitung(a, op, b) {
  if (Number.isNaN(a) || Number.isNaN(b)) return "";
  let hasil;
  switch (op) {
    case "+":
      hasil = a + b;
      break;
    case "-":
      hasil = a - b;
      break;
    case "*":
      hasil = a * b;
      break;
    case "/":
      if (b === 0) return "Tidak bisa dibagi 0";
      hasil = a / b;
      break;
    default:
      return "";
  }
  return formatAngka(hasil);
}

function keAngka(teks) {
  const t = (teks || "").trim();
  return t === "" ? NaN : Number(t);
}

// ===== 2. Hasil langsung muncul saat mengetik di form =====
const inputA = document.getElementById("bilangan1");
const inputOp = document.getElementById("operator");
const inputB = document.getElementById("bilangan2");
const inputHasil = document.getElementById("hasil");

function updateHasilForm() {
  inputHasil.value = hitung(
    keAngka(inputA.value),
    inputOp.value,
    keAngka(inputB.value),
  );
}

[inputA, inputOp, inputB].forEach((el) => {
  el.addEventListener("input", updateHasilForm);
  el.addEventListener("change", updateHasilForm);
});
updateHasilForm(); // untuk mode edit (form sudah terisi)

// ===== 3. Isi kolom hasil & rapikan angka di tabel =====
document.querySelectorAll("td.angka").forEach((td) => {
  td.textContent = formatAngka(Number(td.textContent));
});

document.querySelectorAll("td.hasil").forEach((td) => {
  const { a, op, b } = td.dataset;
  const teks = hitung(Number(a), op, Number(b));
  td.textContent = teks;
  if (teks.startsWith("Tidak")) td.classList.add("gagal");
});

// ===== 4. Efek Spotlight Card (cahaya mengikuti mouse) =====
document.querySelectorAll(".spotlight").forEach((card) => {
  card.addEventListener("mousemove", (e) => {
    const rect = card.getBoundingClientRect();
    card.style.setProperty("--mx", e.clientX - rect.left + "px");
    card.style.setProperty("--my", e.clientY - rect.top + "px");
  });
});

// ===== 5. Efek Click Spark (percikan saat klik) =====
const canvas = document.getElementById("spark-canvas");
const ctx = canvas.getContext("2d");
let sparks = [];
let berjalan = false;

function ukuranCanvas() {
  canvas.width = window.innerWidth;
  canvas.height = window.innerHeight;
}
ukuranCanvas();
window.addEventListener("resize", ukuranCanvas);

document.addEventListener("click", (e) => {
  const mulai = performance.now();
  for (let i = 0; i < 8; i++) {
    sparks.push({
      x: e.clientX,
      y: e.clientY,
      sudut: (2 * Math.PI * i) / 8,
      mulai,
    });
  }
  if (!berjalan) {
    berjalan = true;
    requestAnimationFrame(gambar);
  }
});

function gambar(sekarang) {
  const durasi = 400;
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  sparks = sparks.filter((s) => sekarang - s.mulai < durasi);

  sparks.forEach((s) => {
    const p = Math.max(0, (sekarang - s.mulai) / durasi);
    const halus = p * (2 - p); // easing keluar
    const jarak = halus * 22;
    const panjang = 10 * (1 - halus);
    const x1 = s.x + jarak * Math.cos(s.sudut);
    const y1 = s.y + jarak * Math.sin(s.sudut);
    const x2 = s.x + (jarak + panjang) * Math.cos(s.sudut);
    const y2 = s.y + (jarak + panjang) * Math.sin(s.sudut);

    ctx.strokeStyle = "#fff";
    ctx.lineWidth = 2;
    ctx.beginPath();
    ctx.moveTo(x1, y1);
    ctx.lineTo(x2, y2);
    ctx.stroke();
  });

  if (sparks.length > 0) {
    requestAnimationFrame(gambar);
  } else {
    berjalan = false;
  }
}

// ===== 6. Pesan sukses hilang otomatis =====
const toast = document.querySelector(".toast");
if (toast) {
  setTimeout(() => toast.classList.add("hilang"), 2800);
  setTimeout(() => toast.remove(), 3300);
}
