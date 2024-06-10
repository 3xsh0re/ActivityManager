var adminApp = new Vue({
  el: '#admin-app',
  data: {
    currentView: 'activities',
    activities: [],
    resources: [],
    users: [],
    showActivityModal: false,
    showApproveModal: false,
    showAddModal: false,
    showEditModal: false,
    selectedActivity: {},
    selectedResource: {},
    newResource: {
      resourceName: '',
      quantity: 0,
      type: 0
    },
    approvalStatus: null,
    approvalResult: '',
    page: 1,
    pageSize: 10,
    totalActivities: 0,
    totalResources: 0,
    totalUsers: 0
  },
  created: function() {
    axios.interceptors.request.use(config => {
      const token = this.getCookie('token');
      const uid = this.getCookie('uid');
      if (token) {
        config.headers['Authorization'] = token;
      }
      if (uid) {
        config.headers['uid'] = uid;
      }
      return config;
    }, error => {
      return Promise.reject(error);
    });
    this.fetchActivities();
    this.fetchResources();
    this.fetchUsers();
  },
  methods: {
    getCookie: function(name) {
      const value = `; ${document.cookie}`;
      const parts = value.split(`; ${name}=`);
      if (parts.length === 2) return parts.pop().split(';').shift();
    },
    formatDateTime: function(isoString) {
      const date = new Date(isoString);
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();
      const hours = date.getHours();
      const minutes = date.getMinutes().toString().padStart(2, '0');
      return `${year}年${month}月${day}日${hours}:${minutes}`;
    },
    fetchActivities: function() {
      axios.post('http://47.93.254.31:18088/admin/getAllActivity', {
        page: this.page,
        pageSize: this.pageSize
      })
      .then(response => {
        if (response.data.msg === null) {
          this.activities = response.data.data.records.map(activity => {
            return {
              id: activity.id,
              actName: activity.actName,
              beginTime: this.formatDateTime(activity.beginTime),
              endTime: this.formatDateTime(activity.endTime),
              actDescription: activity.actDescription,
              status: activity.status,
              place: activity.place
            };
          });
          this.totalActivities = response.data.data.total;
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
      axios.post('http://47.93.254.31:18088/resource/getAllResource', {
        page: this.page,
        pageSize: this.pageSize
      })
      .then(response => {
        if (response.data.msg == null) {
          this.resources = response.data.data.records.map(resource => ({
            id: resource.id,
            resourceName: resource.resourceName,
            type: resource.type,
            quantity: resource.quantity
          }));
          this.totalResources = response.data.data.total;
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
      axios.post('http://47.93.254.31:18088/admin/getAllUser', {
        page: this.page,
        pageSize: this.pageSize
      })
      .then(response => {
        if (response.data.msg === null) {
          this.users = response.data.data.records.map(user => ({
            id: user.id,
            username: user.username,
            phoneNumber: user.phoneNumber,
            email: user.email
          }));
          this.totalUsers = response.data.data.total;
        } else {
          console.error('获取用户列表失败：', response.data.msg);
          alert('获取用户列表失败');
        }
      })
      .catch(error => {
        console.error('获取用户列表失败：', error);
        alert('获取用户列表失败');
      });
    },
    deleteUser: function(userId) {
      axios.get(`http://47.93.254.31:18088/admin/deleteUser?deleteId=${userId}`)
      .then(response => {
        if (response.data.code === 1) {
          alert('用户删除成功');
          this.fetchUsers();
        } else {
          alert('用户删除失败: ' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('删除用户失败：', error);
        alert('删除用户失败');
      });
    },
    deleteActivity: function(activityId, userId) {
      axios.get(`http://47.93.254.31:18088/activity/deleteActivity?aid=${activityId}&uid=${userId}`)
      .then(response => {
        if (response.data.code === 1) {
          alert('活动删除成功');
          this.fetchActivities();
        } else {
          alert('活动删除失败: ' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('删除活动失败：', error);
        alert('删除活动失败');
      });
    },
    viewActivity: function(activityId) {
      axios.get(`http://47.93.254.31:18088/activity/getActInfoToAll?aid=${activityId}`)
      .then(response => {
        if (response.data.code === 1) {
          this.selectedActivity = response.data.data;
          this.showActivityModal = true;
        } else {
          alert('获取活动信息失败: ' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('获取活动信息失败：', error);
        alert('获取活动信息失败');
      });
    },
    approveActivity: function() {
      axios.get(`http://47.93.254.31:18088/admin/checkActContent?aid=${this.selectedActivity.id}&status=${this.approvalStatus}&result=${this.approvalResult}`)
      .then(response => {
        if (response.data.code === 1) {
          alert('活动审核成功');
          this.closeApproveModal();
          this.fetchActivities();
        } else {
          alert('活动审核失败: ' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('活动审核失败：', error);
        alert('活动审核失败');
      });
    },
    changePage: function(newPage) {
      this.page = newPage;
      this.fetchActivities();
      this.fetchResources();
      this.fetchUsers();
    },
    totalPages: function(total) {
      return Math.ceil(total / this.pageSize);
    },
    openApproveModal: function(activityId) {
      this.selectedActivity = this.activities.find(activity => activity.id === activityId);
      this.approvalStatus = null;
      this.approvalResult = '';
      this.showApproveModal = true;
    },
    closeApproveModal: function() {
      this.showApproveModal = false;
    },
    closeActivityModal: function() {
      this.showActivityModal = false;
    },
    openAddModal: function() {
      this.newResource = {
        resourceName: '',
        quantity: 0,
        type: 0
      };
      this.showAddModal = true;
    },
    closeAddModal: function() {
      this.showAddModal = false;
    },
    addResource: function() {
      axios.post('http://47.93.254.31:18088/resource/resourceAddition', {
        resource: this.newResource.resourceName,
        quantity: this.newResource.quantity,
        type: this.newResource.type
      })
      .then(response => {
        if (response.data.code === 1) {
          alert('资源添加成功');
          this.fetchResources();
          this.closeAddModal();
        } else {
          alert('资源添加失败: ' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('资源添加失败：', error);
        alert('资源添加失败');
      });
    },
    openEditModal: function(resource) {
      this.selectedResource = Object.assign({}, resource);
      this.showEditModal = true;
    },
    closeEditModal: function() {
      this.showEditModal = false;
    },
    editResource: function() {
      axios.post('http://47.93.254.31:18088/resource/resourceAddition', {
        resource: this.selectedResource.resourceName,
        quantity: this.selectedResource.quantity,
        type: this.selectedResource.type
      })
      .then(response => {
        if (response.data.code === 1) {
          alert('资源修改成功');
          this.fetchResources();
          this.closeEditModal();
        } else {
          alert('资源修改失败: ' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('资源修改失败：', error);
        alert('资源修改失败');
      });
    }
  },
});
