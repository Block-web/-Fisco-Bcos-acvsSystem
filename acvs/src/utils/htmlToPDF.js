// 页面导出为pdf格式
import html2Canvas from "html2canvas";
import jsPDF from "jspdf";

const htmlToPdf = {
  getPdf(title, id) {
    // 1. 构建 DOM 树
    const pdfContent = document.querySelector(id);
    pdfContent.ownerDocument.defaultView.innerHeight = 1000;
    pdfContent.ownerDocument.defaultView.innerWidth = 1000;
    // 2. Canvas 渲染图像
    html2Canvas(pdfContent, {
      logging: false,
      useCORS: true,
      scale: 2,//按比例增加分辨率
    }).then((canvas) => {
     // 3. jsPDF 导出 pdf文件
      const imgData = canvas.toDataURL("image/jpeg", 1.0);
      var pdf = new jsPDF("p", "mm", "a4");
      const a4w = 190;
      const a4h = 277;
      pdf.addImage(
        imgData,
        "JPEG",
        10,
        10,
        a4w,
        Math.min(a4h, (a4w * canvas.height) / canvas.width)
      );
      pdf.save(title + ".pdf");
    });
  },
};
export default htmlToPdf;