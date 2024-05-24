var adminApp = new Vue({
  el: '#admin-app',
  data: {
    currentView: 'activities',
    activities: [],
    resources: [],
    users: [],
    reimbursements: [],
    showActivityModal: false,
    selectedActivity: {}
  },
  created: function() {
    this.fetchActivities();
    this.fetchResources();
    this.fetchUsers();
    this.fetchReimbursements();
  },
  methods: {
    formatDateTime: function(isoString) {
      const date = new Date(isoString);
      const year = date.getFullYear();
      const month = date.getMonth() + 1; // 月份从0开始，需要加1
      const day = date.getDate();
      const hours = date.getHours();
      const minutes = date.getMinutes().toString().padStart(2, '0');
      return `${year}年${month}月${day}日${hours}:${minutes}`;
    },
    fetchActivities: function() {
      axios.get('http://47.93.254.31:18088/admin/getAllActivity')
        .then(response => {
          if (response.data.msg === null) {
            this.activities = response.data.data.map(activity => ({
              orgId: activity.orgId,
              username: activity.username,
              actName: activity.actName,
              actDescription: activity.actDescription,
              status: activity.status,
              beginTime: this.formatDateTime(activity.beginTime),
              endTime: this.formatDateTime(activity.endTime),
              totalBudget: activity.totalBudget,
              place: activity.place,
              rank: activity.rank
            }));
          } else {
            console.error('获取活动列表失败：', response.data.msg);
            alert('获取活动列表失败');
          }
        })
        .catch(error => {
          console.error('获取活动列表失败：', error);
          alert('获取活动列表失败');
        });
    },
    fetchResources: function() {
      axios.get('http://47.93.254.31:18088/resource/getAllResource')
        .then(response => {
          if (response.data.msg == null) {
            this.resources = response.data.data.map(resource => ({
              id: resource.id,
              resourceName: resource.resourceName,
              type: resource.type,
              quantity: resource.quantity
            }));
          } else {
            console.error('获取资源列表失败：', response.data.msg);
            alert('获取资源列表失败');
          }
        })
        .catch(error => {
          console.error('获取资源列表失败：', error);
          alert('获取资源列表失败');
        });
    },
    fetchUsers: function() {
      axios.get('http://47.93.254.31:18088/admin/getAllUser')
        .then(response => {
          if (response.data.msg === null) {
            this.users = response.data.data.map(user => ({
              id: user.id,
              username: user.username,
              phoneNumber: user.phoneNumber,
              email: user.email
            }));
          } else {
            console.error('获取用户列表：', response.data.msg);
            alert('获取用户列表失败');
          }
        })
        .catch(error => {
          console.error('获取用户列表失败：', error);
          alert('获取用户列表失败');
        });
    },
    fetchReimbursements: function() {
      axios.get('http://47.93.254.31:18088/admin/getReimbursements')
        .then(response => {
          if (response.data.msg === null) {
            this.reimbursements = response.data.data.map(reimbursement => ({
              id: reimbursement.id,
              reimbursementid: reimbursement.reimbursementid,
              status: reimbursement.status
            }));
          } else {
            console.error('获取报销审核列表失败：', response.data.msg);
            alert('获取报销审核列表失败');
          }
        })
        .catch(error => {
          console.error('获取报销审核列表失败：', error);
          alert('获取报销审核列表失败');
        });
    },
    viewActivity: function(activity) {
      this.selectedActivity = activity;
      this.showActivityModal = true;
    },
    closeActivityModal: function() {
      this.showActivityModal = false;
    },
    approveActivity: function(orgId) {
      const activity = this.activities.find(act => act.orgId === orgId);
      if (activity) {
        activity.status = 1; // 设置状态为1表示审核通过
        // 这里可以添加额外的API请求以在服务器端更新状态
      }
    },
    deleteActivity: function(orgId) {
      this.activities = this.activities.filter(act => act.orgId !== orgId);
      // 这里可以添加额外的API请求以在服务器端删除活动
    }
  }
});
