new Vue({
  el: '#app',
  data: {
    showActivityList: true,
    showUserProfile: false,
    showInfo: true,
    showMyActivities: false,
    showCreateActivityModal: false,
    selectedTemplate: 'custom',
    newActivity: {
      title: '',
      details: '',
      agenda: '',
      participants: '',
      materials: ''
    },
    activities: [],
    userProfile: {},
    userActivities: []
  },
  methods: {
    showMessage: function(message) {
      alert(message);
    },
    switchToProfile: function() {
      this.showActivityList = false;
      this.showUserProfile = true;
      this.switchToInfo();
    },
    switchToMain: function() {
      this.showActivityList = true;
      this.showUserProfile = false;
    },
    switchToInfo: function() {
      this.showInfo = true;
      this.showMyActivities = false;
    },
    switchToMyActivities: function() {
      this.showInfo = false;
      this.showMyActivities = true;
    },
    getProgressClass: function(status) {
      switch (status) {
        case '筹备中':
          return 'status-preparing';
        case '进行中':
          return 'status-ongoing';
        case '已完成':
          return 'status-completed';
        default:
          return '';
      }
    },
    fetchData: function() {
      axios.post('http://47.93.254.31:18088/activity/list')
        .then(response => {
          this.activities = response.data.activities;
        })
        .catch(error => {
          console.error('获取活动列表失败：', error);
          this.showMessage('获取活动列表失败');
        });

      axios.post('http://47.93.254.31:18088/user/profile')
        .then(response => {
          this.userProfile = response.data.userProfile;
        })
        .catch(error => {
          console.error('获取用户个人信息失败：', error);
          this.showMessage('获取用户个人信息失败');
        });

      axios.post('http://47.93.254.31:18088/user/activities')
        .then(response => {
          this.userActivities = response.data.userActivities;
        })
        .catch(error => {
          console.error('获取用户参与的活动列表失败：', error);
          this.showMessage('获取用户参与的活动列表失败');
        });
    },
    openCreateActivityModal: function() {
      this.showCreateActivityModal = true;
    },
    closeCreateActivityModal: function() {
      this.showCreateActivityModal = false;
    },
    createActivity: function() {
      const newActivityData = {
        title: this.newActivity.title,
        details: this.selectedTemplate === 'custom' ? this.newActivity.details : '',
        agenda: this.selectedTemplate === 'meeting' ? this.newActivity.agenda : '',
        participants: this.selectedTemplate === 'meeting' ? this.newActivity.participants : '',
        materials: this.selectedTemplate === 'meeting' ? this.newActivity.materials : ''
      };

      axios.post('http://47.93.254.31:18088/activity/create', newActivityData)
        .then(response => {
          if (response.data.success) {
            this.showMessage('创建活动成功');
            this.closeCreateActivityModal();
            this.fetchData();
          } else {
            this.showMessage('创建活动失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('创建活动失败：', error);
          this.showMessage('创建活动失败');
        });
    }
  },
  mounted() {
    this.fetchData();
  }
});
