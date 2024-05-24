var app = new Vue({
    el: '#app',
    data: {
      // 注册
      username: '',
      mail: '',
      phoneNumber: '',
      passwd: '',
      passwd_again: '',
      captcha: '',
      // 登录
      loginPhone: '',
      loginEmail: '',
      loginPassword: '',
      user: null,
      // 修改密码
      revisePhone: '',
      revisePassword: '',
      reviseCaptcha: '',
      // 登录类型
      loginType: 0,
      
      showRegister: '',
      showLogin: true,
      showRevise: '',
      verificationCode: ''
    },
  
    methods: {
      showMessage: function(message) {
        alert(message);
      },
  
      login: function() {
        // 模拟登录逻辑
        // 发送POST请求到登录接口
        if (!this.loginPhone || !this.loginPassword || !this.verificationCode) {
          this.showMessage('请输入完整的信息');
        } else {
          axios.post('http://47.93.254.31:18088/user/login', {
            phoneNumber: this.loginPhone,
            passwd: this.loginPassword,
            captcha: this.verificationCode,
            loginType: this.loginType
          })
          .then(response => {
            // 处理登录成功的响应
            console.log(response.data);
            if (response.data.msg === null) {
                if(this.loginType==0){
                    this.showMessage('登录成功');
                    window.location.href = "test2.html";
                }
                else{
                    this.showMessage('登录成功');
                    window.location.href = "test3.html";
                }
            } else {
              this.showMessage('登录失败：' + response.data.msg);
            }
          })
          .catch(error => {
            // 处理登录失败的情况
            console.error('登录失败：', error);
            this.showMessage('登录失败');
          });
        }
        // 清空输入框
        this.loginPhone = '';
        this.loginPassword = '';
        this.verificationCode = '';
      },
  
      revise: function() {
        // 模拟修改密码逻辑
        // 发送POST请求到修改密码接口
        const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,15}$/;
  
        if (!this.revisePhone || !this.revisePassword || !this.reviseCaptcha) {
          this.showMessage('请输入完整的信息');
        } else if (!passwordRegex.test(this.passwd)) {
          this.showMessage('密码由6-15位组成，且包含字母、数字和特殊字符');
        } else {
          axios.post('http://47.93.254.31:18088/user/resetPasswd', {
            phoneNumber: this.revisePhone,
            passwd: this.revisePassword,
            captcha: this.reviseCaptcha
          })
          .then(response => {
            console.log(response.data);
            if (response.data.msg === null) {
              this.showMessage('修改成功');
              this.showRegister = false;
              this.showLogin = true;
              this.showRevise = false;
            } else {
              this.showMessage('修改失败：' + response.data.msg);
            }
          })
          .catch(error => {
            console.error('修改失败：', error);
            this.showMessage('修改失败');
          });
        }
        // 清空输入框
        this.revisePhone = '';
        this.revisePassword = '';
        this.reviseCaptcha = '';
      },
  
      sendVerificationCode: function() {
        if(this.showLogin){
            axios.get('http://47.93.254.31:18088/user/getCaptchaByPhone', {
                params: {
                phoneNumber: this.loginPhone
                } 
            })
            .then(response => {
                console.log(response.data);
                if (response.data.msg === null) {
                  this.showMessage('验证码已发送，请注意查收');
                } else {
                  this.showMessage('发送验证码失败：' + response.data.msg);
                }
              })
              .catch(error => {
                console.error('发送验证码失败：', error);
                this.showMessage('发送验证码失败');
              });
        }
        else if(this.showRevise){
            axios.get('http://47.93.254.31:18088/user/getCaptchaByPhone', {
                params: {
                phoneNumber: this.revisePhone
                } 
            })
            .then(response => {
                console.log(response.data);
                if (response.data.msg === null) {
                  this.showMessage('验证码已发送，请注意查收');
                } else {
                  this.showMessage('发送验证码失败：' + response.data.msg);
                }
              })
              .catch(error => {
                console.error('发送验证码失败：', error);
                this.showMessage('发送验证码失败');
              });
        }
      },

  
      register: function() {
        // 正则表达式定义
        const emailRegex = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
        const phoneRegex = /^1[3456789]\d{9}$/;
        const usernameRegex = /^[a-zA-Z0-9_]{4,12}$/;
        const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,15}$/;
  
        // 验证输入是否合法
        if (!this.username || !this.phoneNumber || !this.passwd || !this.passwd_again || !this.mail || !this.captcha) {
          this.showMessage('请输入完整的注册信息');
        } else if (!emailRegex.test(this.mail)) {
          this.showMessage('请输入正确的邮箱格式');
        } else if (!phoneRegex.test(this.phoneNumber)) {
          this.showMessage('请输入正确的手机号格式');
        } else if (!usernameRegex.test(this.username)) {
          this.showMessage('用户名由4-12位字符组成');
        } else if (!passwordRegex.test(this.passwd)) {
          this.showMessage('密码由6-15位组成，且包含字母、数字和特殊字符');
        } else if (this.passwd_again !== this.passwd) {
          this.showMessage('密码不一致，请重新输入');
        } else {
          // 发送POST请求到注册接口
          axios.post('http://47.93.254.31:18088/user/register', {
            username: this.username,
            phoneNumber: this.phoneNumber,
            passwd: this.passwd,
            email: this.mail,
            captcha: this.captcha
          })
          .then(response => {
            // 处理注册成功的响应
            console.log(response.data);
            // 模拟注册成功，显示消息提示
            this.showMessage('注册成功，请登录');
            // 切换回登录界面
            this.showRegister = false;
          })
          .catch(error => {
            // 处理注册失败的情况
            console.error('注册失败：', error);
            this.showMessage('注册失败');
          });
        }
        // 清空输入框
        this.username = '';
        this.mail = '';
        this.phoneNumber = '';
        this.passwd = '';
        this.passwd_again = '';
        this.captcha = '';
      },
  
      switchToRegister: function() {
        // 切换到注册界面
        this.showRegister = true;
        this.showLogin = false;
        //   this.showMessage('');
      },
      switchToLogin: function() {
        // 切换回登录界面
        this.showRegister = false;
        this.showLogin = true;
        this.showRevise = false;
        //   this.showMessage('');
      },
      forgotPassword: function() {
        this.showRevise = true;
        this.showLogin = false;
      }
    }
  });
  