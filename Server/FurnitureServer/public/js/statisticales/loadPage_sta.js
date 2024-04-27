 // Gọi hàm vẽ biểu đồ khi DOM đã tải
 document.addEventListener("DOMContentLoaded", function() {
    const ctx = document.getElementById('revenueChart').getContext('2d');

    // Lấy dữ liệu từ monthlyRevenueData
    const chartData = {
        labels: Object.keys(monthlyRevenueData), // Lấy các tháng
        datasets: [{
            label: 'Doanh thu theo tháng',
            data: Object.values(monthlyRevenueData) // Lấy doanh thu theo tháng
        }]
    };

    const chartOptions = {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    };

    // Vẽ biểu đồ
    const revenueChart = new Chart(ctx, {
        type: 'bar',
        data: chartData,
        options: chartOptions
    });
});