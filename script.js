const avatarInput = document.getElementById('avatar-upload');
const avatarImg = document.getElementById('edit-avatar');
const nameInput = document.getElementById('name');
const majorInput = document.getElementById('major');
const phoneInput = document.getElementById('phone');
const emailInput = document.getElementById('email');
const detailInput = document.getElementById('detail');
const saveBtn = document.getElementById('save-btn');
const cancelBtn = document.getElementById('cancel-btn');
const closeBtn = document.getElementById('close-btn');
const editBtn = document.getElementById('edit-btn');
const themeToggle = document.getElementById('theme-toggle');
const modalOverlay = document.getElementById('modal-overlay');
const skillsList = document.getElementById('skills-list');
const newSkillInput = document.getElementById('new-skill');
const addSkillBtn = document.getElementById('add-skill-btn');

// 展示区域元素
const viewAvatar = document.getElementById('view-avatar');
const viewName = document.getElementById('view-name');
const viewMajor = document.getElementById('view-major');
const viewPhone = document.getElementById('view-phone');
const viewEmail = document.getElementById('view-email');
const viewDetail = document.getElementById('view-detail');
const viewSkills = document.getElementById('view-skills');

let skills = ['HTML', 'CSS', 'JavaScript'];

function loadData() {
    const savedName = localStorage.getItem('userName');
    const savedMajor = localStorage.getItem('userMajor');
    const savedAvatar = localStorage.getItem('userAvatar');
    const savedTheme = localStorage.getItem('theme');
    const savedPhone = localStorage.getItem('userPhone');
    const savedEmail = localStorage.getItem('userEmail');
    const savedSkills = localStorage.getItem('userSkills');
    const savedDetail = localStorage.getItem('userDetail');
    
    if (savedName) {
        viewName.textContent = savedName;
    }
    if (savedMajor) {
        viewMajor.textContent = savedMajor;
    }
    if (savedAvatar) {
        viewAvatar.src = savedAvatar;
    }
    if (savedPhone) {
        viewPhone.textContent = savedPhone;
    }
    if (savedEmail) {
        viewEmail.textContent = savedEmail;
    }
    if (savedSkills) {
        skills = JSON.parse(savedSkills);
        renderViewSkills();
    }
    if (savedDetail) {
        viewDetail.textContent = savedDetail;
    }
    if (savedTheme === 'dark') {
        document.body.classList.add('dark-mode');
        themeToggle.textContent = '☀️';
    }
}

function renderViewSkills() {
    viewSkills.innerHTML = '';
    skills.forEach(skill => {
        const tag = document.createElement('span');
        tag.className = 'skill-tag';
        tag.textContent = skill;
        viewSkills.appendChild(tag);
    });
}

function renderEditSkills() {
    skillsList.innerHTML = '';
    skills.forEach((skill, index) => {
        const tag = document.createElement('span');
        tag.className = 'skill-tag';
        tag.innerHTML = `${skill}<span class="remove-skill" data-index="${index}">×</span>`;
        skillsList.appendChild(tag);
    });
    
    document.querySelectorAll('.remove-skill').forEach(btn => {
        btn.addEventListener('click', function() {
            const index = parseInt(this.dataset.index);
            skills.splice(index, 1);
            renderEditSkills();
        });
    });
}

function addSkill() {
    const skill = newSkillInput.value.trim();
    if (skill && !skills.includes(skill)) {
        skills.push(skill);
        renderEditSkills();
        newSkillInput.value = '';
    }
}

function openModal() {
    // 将当前展示的数据填充到编辑表单
    nameInput.value = viewName.textContent;
    majorInput.value = viewMajor.textContent;
    phoneInput.value = viewPhone.textContent;
    emailInput.value = viewEmail.textContent;
    detailInput.value = viewDetail.textContent;
    avatarImg.src = viewAvatar.src;
    renderEditSkills();
    
    modalOverlay.classList.add('active');
}

function closeModal() {
    modalOverlay.classList.remove('active');
}

function saveData() {
    const name = nameInput.value.trim();
    const major = majorInput.value.trim();
    const phone = phoneInput.value.trim();
    const email = emailInput.value.trim();
    const detail = detailInput.value.trim();

    if (name === '') {
        alert('请输入姓名');
        nameInput.focus();
        return;
    }

    // 保存到 localStorage
    localStorage.setItem('userName', name);
    localStorage.setItem('userMajor', major);
    localStorage.setItem('userAvatar', avatarImg.src);
    localStorage.setItem('userPhone', phone);
    localStorage.setItem('userEmail', email);
    localStorage.setItem('userSkills', JSON.stringify(skills));
    localStorage.setItem('userDetail', detail);

    // 更新展示区域
    viewName.textContent = name;
    viewMajor.textContent = major || '暂无专业班级';
    viewPhone.textContent = phone || '暂无电话';
    viewEmail.textContent = email || '暂无邮箱';
    viewDetail.textContent = detail || '暂无详细介绍';
    viewAvatar.src = avatarImg.src;
    renderViewSkills();
    
    closeModal();
    alert('保存成功！');
}

// 初始化
loadData();

// 事件监听
themeToggle.addEventListener('click', function() {
    document.body.classList.toggle('dark-mode');
    const isDark = document.body.classList.contains('dark-mode');
    themeToggle.textContent = isDark ? '☀️' : '🌙';
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
});

editBtn.addEventListener('click', openModal);
closeBtn.addEventListener('click', closeModal);
cancelBtn.addEventListener('click', closeModal);

modalOverlay.addEventListener('click', function(e) {
    if (e.target === modalOverlay) {
        closeModal();
    }
});

avatarInput.addEventListener('change', function(event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            avatarImg.src = e.target.result;
        };
        reader.readAsDataURL(file);
    }
});

addSkillBtn.addEventListener('click', addSkill);
newSkillInput.addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        addSkill();
    }
});

saveBtn.addEventListener('click', saveData);

// 动态背景 - 缓慢漂浮的透明大泡泡
const canvas = document.getElementById('bg-canvas');
const ctx = canvas.getContext('2d');

let bubbles = [];
let animationId;

function resizeCanvas() {
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
}

class Bubble {
    constructor() {
        this.x = Math.random() * canvas.width;
        this.y = canvas.height + Math.random() * 200;
        this.radius = Math.random() * 80 + 40;
        this.vx = (Math.random() - 0.5) * 0.3;
        this.vy = -(Math.random() * 0.5 + 0.2);
        this.opacity = Math.random() * 0.15 + 0.05;
        // 使用蓝紫色调
        this.hue = Math.random() * 40 + 220;
    }

    update() {
        this.x += this.vx;
        this.y += this.vy;

        // 左右轻微摆动
        this.x += Math.sin(this.y * 0.01) * 0.2;

        // 如果泡泡飘出顶部，重置到底部
        if (this.y < -this.radius * 2) {
            this.y = canvas.height + this.radius;
            this.x = Math.random() * canvas.width;
        }
    }

    draw() {
        ctx.save();
        ctx.globalAlpha = this.opacity;
        
        // 创建径向渐变
        const gradient = ctx.createRadialGradient(
            this.x - this.radius * 0.3, 
            this.y - this.radius * 0.3, 
            0,
            this.x, 
            this.y, 
            this.radius
        );
        
        // 使用中性色调，日间夜间都清晰可见
        gradient.addColorStop(0, `hsla(${this.hue}, 60%, 70%, 0.25)`);
        gradient.addColorStop(0.5, `hsla(${this.hue}, 50%, 60%, 0.12)`);
        gradient.addColorStop(1, `hsla(${this.hue}, 40%, 50%, 0)`);
        
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.radius, 0, Math.PI * 2);
        ctx.fillStyle = gradient;
        ctx.fill();
        
        // 添加高光效果
        ctx.beginPath();
        ctx.arc(
            this.x - this.radius * 0.3, 
            this.y - this.radius * 0.3, 
            this.radius * 0.2, 
            0, 
            Math.PI * 2
        );
        ctx.fillStyle = 'rgba(255, 255, 255, 0.3)';
        ctx.fill();
        
        ctx.restore();
    }
}

function initBubbles() {
    bubbles = [];
    const bubbleCount = Math.floor((canvas.width * canvas.height) / 40000);
    for (let i = 0; i < bubbleCount; i++) {
        const bubble = new Bubble();
        bubble.y = Math.random() * canvas.height;
        bubbles.push(bubble);
    }
}

function animate() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    bubbles.forEach(bubble => {
        bubble.update();
        bubble.draw();
    });

    animationId = requestAnimationFrame(animate);
}

// 初始化
resizeCanvas();
initBubbles();
animate();

// 窗口大小改变时重新初始化
window.addEventListener('resize', () => {
    resizeCanvas();
    initBubbles();
});
