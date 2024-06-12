new Vue({
  el: '#app',
  data: {
    showActivityList: true,
    showUserProfile: false,
    showInfo: true,
    showMyActivities: false,
    showMyExpenses: false,
    showCreateActivityModal: false,
    showActivityDetailsModal1: false,
    showActivityDetailsModal2: false,
    showEditActivityModal: false,
    showParticipantManagementModal: false,
    showResourceReservationModal: false,
    showUnCheckedUserListModal: false,
    showJoinActivityModal: false,
    showSetBudgetModal: false,
    showCommitExpenseModal: false,
    showExpenseListModal: false,
    showUserNotifications: false,
    showActivityNotifications: false,
    showSendNotificationModal: false,
    showActivityNotificationsModal: false,
    showFileManagementModal: false,
    showReminders: false,
    selectedTemplate: 'custom',
    newActivity: {
      activityName: '',
      activityDescription: '',
      beginTime: '',
      endTime: '',
      roleList: {},
      ifFileStore: true,
      actStatus: {}
    },
    newRole: { name: '' },
    newStatus: { name: '', order: '' },
    activities: [],
    userProfile: {},
    userActivities: [],
    userSchedule: [],
    userExpenses: [],
    activityExpenses: [],
    userNotifications: [],
    notificationContent: '',
    activityNotifications: [],
    reminders: [],
    files: [],
    comments: [],
    selectedActivity: null,
    editActivity: null,
    page: 1,
    pageSize: 5,
    total: 0,
    expensePage: 1,
    expensePageSize: 10,
    expenseTotal: 0,
    notificationPage: 1,
    notificationPageSize: 10,
    notificationTotal: 0,
    filePage: 1,
    filePageSize: 10,
    fileTotal: 0,
    commentPage: 1,
    commentPageSize: 10,
    commentTotal: 0,
    reminderPage: 1,
    reminderPageSize: 10,
    reminderTotal: 0,
    participantUid: '',
    participantGroup: '',
    participantRole: '',
    selectedActivityId: null,
    budget: 0,
    expense: {
      content: '',
      cost: ''
    },
    resourceReservation: {
      resource: '',
      quantity: '',
    },
    unCheckedUserList: [],
    joinActivityReason: '',
    newReminder: {
      content: '',
      reminderTime: ''
    },
    newComment: {
      content: ''
    },
    chatMessages: [], // 聊天消息
    newMessage: '',    // 新消息内容
    selectedFile: null
  },

  computed: {
    isOrganizer() {
      return this.selectedActivity && this.userProfile && this.selectedActivity.orgId === this.userProfile.id;
    }
  },
  
  methods: {
    getCookie: function(name) {
      const value = `; ${document.cookie}`;
      const parts = value.split(`; ${name}=`);
      if (parts.length === 2) return parts.pop().split(';').shift();
    },
    formatDateTime: function(dateString) {
      const date = new Date(dateString);
    
      // 获取 UTC 时间
      const year = date.getUTCFullYear();
      const month = date.getUTCMonth() + 1; // 月份从0开始，需要加1
      const day = date.getUTCDate();
      const hours = date.getUTCHours();
      const minutes = date.getUTCMinutes();
    
      // 格式化时间
      return `${year}年${month}月${day}日${hours}:${minutes < 10 ? '0' + minutes : minutes}`;
    },
    
    showMessage: function(message) {
      alert(message);
    },
    switchToProfile: function() {
      this.showActivityList = false;
      this.showUserProfile = true;
      this.showFileManagementModal = false;
      this.switchToInfo();
    },
    switchToMain: function() {
      this.showActivityList = true;
      this.showUserProfile = false;
      this.showFileManagementModal = false;
      this.fetchData();
    },
    switchToInfo: function() {
      this.showInfo = true;
      this.showMyActivities = false;
      this.showMyExpenses = false;
      this.showUserNotifications = false;
      this.showActivityNotifications = false;
      this.showReminders = false;
    },
    switchToMyActivities: function() {
      this.showInfo = false;
      this.showMyActivities = true;
      this.showMyExpenses = false;
      this.showUserNotifications = false;
      this.showActivityNotifications = false;
      this.showReminders = false;
      this.fetchUserSchedule();
    },
    switchToMyExpenses: function() {
      this.showInfo = false;
      this.showMyActivities = false;
      this.showMyExpenses = true;
      this.showUserNotifications = false;
      this.showActivityNotifications = false;
      this.showReminders = false;
      this.fetchUserExpenses();
    },
    switchToUserNotifications: function() {
      this.showInfo = false;
      this.showMyActivities = false;
      this.showMyExpenses = false;
      this.showUserNotifications = true;
      this.showActivityNotifications = false;
      this.showReminders = false;
      this.fetchUserNotifications();
    },
    switchToActivityNotifications: function(aid) {
      this.showActivityNotificationsModal = true;
      this.selectedActivityId = aid;
      this.fetchActivityNotifications();
    },
    switchToReminders: function() {
      this.showInfo = false;
      this.showMyActivities = false;
      this.showMyExpenses = false;
      this.showUserNotifications = false;
      this.showActivityNotifications = false;
      this.showReminders = true;
      this.fetchUserReminders();
    },
    getActivityStatus: function(status) {
      switch (status) {
        case 1:
          return '筹备中';
        case 2:
          return '进行中';
        case 3:
          return '已结束';
        default:
          return '';
      }
    },
    getActivityStatus2: function(status) {
      switch (status) {
        case "1":
          return '筹备中';
        case "2":
          return '进行中';
        case "3":
          return '已结束';
        default:
          return '';
      }
    },
    getStatusClass: function(status) {
      switch (status) {
        case 1:
          return 'status-preparing';
        case 2:
          return 'status-ongoing';
        case 3:
          return 'status-completed';
        default:
          return '';
      }
    },
    getStatusClass2: function(status) {
      switch (status) {
        case "1":
          return 'status-preparing';
        case "2":
          return 'status-ongoing';
        case "3":
          return 'status-completed';
        default:
          return '';
      }
    },
    
    fetchData: function() {
      axios.post('http://47.93.254.31:18088/activity/getAllActivity', {
        page: this.page,
        pageSize: this.pageSize
      })
      .then(response => {
        if (response.data.code === 1) {
          this.total = response.data.data.total;
          this.activities = response.data.data.records;
          console.log('total: ' + this.total);
        } else {
          this.showMessage('获取活动列表失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('获取活动列表失败：', error);
        this.showMessage('获取活动列表失败');
      });
      this.fetchUserProfile();
    },
    fetchUserProfile: function() {
      const uid = this.getCookie('uid');
      axios.get(`http://47.93.254.31:18088/user/getUserInfoByUid?uid=${uid}`)
        .then(response => {
          if (response.data.code === 1) {
            this.userProfile = response.data.data;
          } else {
            this.showMessage('获取用户个人信息失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('获取用户个人信息失败：', error);
          this.showMessage('获取用户个人信息失败');
        });
    },
    fetchUserSchedule: function() {
      const uid = this.getCookie('uid');
      axios.get(`http://47.93.254.31:18088/activity/getActSchedule?uid=${uid}`)
        .then(response => {
          if (response.data.code === 1) {
            this.userSchedule = response.data.data;
            this.userSchedule.sort((a, b) => new Date(a.beginTime) - new Date(b.beginTime));
          } else {
            this.showMessage('获取活动日程失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('获取活动日程失败：', error);
          this.showMessage('获取活动日程失败');
        });
    },
    fetchActivityDetails1: function(aid) {
      axios.get(`http://47.93.254.31:18088/activity/getActInfoToAll?aid=${aid}`)
        .then(response => {
          if (response.data.code === 1) {
            this.selectedActivity = response.data.data;
            this.selectedActivity.id=aid;
            // 对actStatus按顺序排序
            this.selectedActivity.actStatus = Object.fromEntries(Object.entries(this.selectedActivity.actStatus).sort((a, b) => a[1] - b[1]));
            this.showActivityDetailsModal1 = true;
            this.fetchComments(aid); // 获取活动评论
          } else {
            this.showMessage('获取活动详情失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('获取活动详情失败：', error);
          this.showMessage('获取活动详情失败');
        });
    },
    sendMessage: function() {
      const messageData = {
        uid: this.userProfile.id,
        aid: this.selectedActivity.id,
        message: this.newMessage
      };
      axios.post('http://47.93.254.31:18088/activity/participantInteractiveSend', messageData)
        .then(response => {
          if (response.data.code === 1) {
            this.newMessage = ''; // 清空输入框
            this.fetchChatMessages(); // 重新获取聊天消息
          } else {
            this.showMessage('发送消息失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('发送消息失败：', error);
          this.showMessage('发送消息失败');
        });
    },
    fetchChatMessages: function() {
      const requestData = {
        uid: this.userProfile.id,
        aid: this.selectedActivity.id
      };
      axios.post('http://47.93.254.31:18088/activity/participantInteractiveReceive', requestData)
        .then(response => {
          if (response.data.code === 1) {
            const rawMessages = response.data.data || [];
            this.chatMessages = [];
            rawMessages.forEach(item => {
              const parsedMessage = JSON.parse(item.message);
              Object.keys(parsedMessage).forEach(timestamp => {
                const [uid, content] = parsedMessage[timestamp].split(':');
                this.chatMessages.push({
                  timestamp: parseInt(timestamp),
                  uid,
                  content
                });
              });
            });
            this.sortMessages(); 
          } else {
            // this.showMessage('获取聊天消息失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('获取聊天消息失败:', error);
          // this.showMessage('获取聊天消息失败');
        });
    },
    sortMessages: function() {
      this.chatMessages.sort((a, b) => a.timestamp - b.timestamp);
    },
    fetchActivityDetails2: function(aid) {
      axios.get(`http://47.93.254.31:18088/activity/getActInfoToAll?aid=${aid}`)
        .then(response => {
          if (response.data.code === 1) {
            this.selectedActivity = response.data.data;
            this.selectedActivity.id=aid;
            // 对actStatus按顺序排序
            this.selectedActivity.actStatus = Object.fromEntries(Object.entries(this.selectedActivity.actStatus).sort((a, b) => a[1] - b[1]));
            this.showActivityDetailsModal2 = true;
            this.fetchComments(aid); // 获取活动评论
            this.fetchChatMessages();
          } else {
            this.showMessage('获取活动详情失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('获取活动详情失败：', error);
          this.showMessage('获取活动详情失败');
        });
    },
    
    fetchEditActivityDetails: function(aid) {
      const uid = this.getCookie('uid');
      axios.get(`http://47.93.254.31:18088/activity/getActInfoToOrganizer?uid=${uid}&aid=${aid}`)
        .then(response => {
          if (response.data.code === 1) {
            this.editActivity = response.data.data;
            // 对actStatus按顺序排序
            this.editActivity.actStatus = Object.fromEntries(Object.entries(this.editActivity.actStatus).sort((a, b) => a[1] - b[1]));
            this.showEditActivityModal = true;
          } else {
            this.showMessage('获取活动编辑信息失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('获取活动编辑信息失败：', error);
          this.showMessage('获取活动编辑信息失败');
        });
    },
    openCreateActivityModal: function() {
      this.newActivity = {
        activityName: '',
        activityDescription: '',
        beginTime: '',
        endTime: '',
        roleList: {},
        ifFileStore: true,
        actStatus: {}
      };
      this.showCreateActivityModal = true;
    },
    closeCreateActivityModal: function() {
      this.showCreateActivityModal = false;
    },
    closeActivityDetailsModal1: function() {
      this.showActivityDetailsModal1 = false;
      this.selectedActivity = null;
    },
    closeActivityDetailsModal2: function() {
      this.showActivityDetailsModal2 = false;
      this.selectedActivity = null;
    },
    closeEditActivityModal: function() {
      this.showEditActivityModal = false;
      this.editActivity = null;
    },
    openParticipantManagementModal: function(aid) {
      this.selectedActivityId = aid;
      this.showParticipantManagementModal = true;
    },
    closeParticipantManagementModal: function() {
      this.showParticipantManagementModal = false;
      this.participantUid = '';
      this.participantGroup = '';
      this.participantRole = '';
    },
    openResourceReservationModal: function(aid) {
      this.selectedActivityId = aid;
      this.showResourceReservationModal = true;
    },
    closeResourceReservationModal: function() {
      this.showResourceReservationModal = false;
      this.resourceReservation = {
        resource: '',
        quantity: '',
      };
    },
    openJoinActivityModal: function(aid) {
      this.selectedActivityId = aid;
      this.showJoinActivityModal = true;
    },
    closeJoinActivityModal: function() {
      this.showJoinActivityModal = false;
      this.joinActivityReason = '';
    },
    openUnCheckedUserListModal: function(aid) {
      this.selectedActivityId = aid;
      this.fetchUnCheckedUserList();
      this.showUnCheckedUserListModal = true;
    },
    closeUnCheckedUserListModal: function() {
      this.showUnCheckedUserListModal = false;
    },
    openSetBudgetModal: function(aid) {
      this.selectedActivityId = aid;
      this.showSetBudgetModal = true;
    },
    closeSetBudgetModal: function() {
      this.showSetBudgetModal = false;
      this.budget = 0;
    },
    openCommitExpenseModal: function(aid) {
      this.selectedActivityId = aid;
      this.showCommitExpenseModal = true;
    },
    closeCommitExpenseModal: function() {
      this.showCommitExpenseModal = false;
      this.expense = {
        content: '',
        cost: ''
      };
    },
    openExpenseListModal: function(aid) {
      this.selectedActivityId = aid;
      this.fetchActivityExpenses();
      this.showExpenseListModal = true;
    },
    closeExpenseListModal: function() {
      this.showExpenseListModal = false;
    },
    openFileManagementModal: function(aid) {
      this.selectedActivityId = aid;
      this.showFileManagementModal = true;
      this.fetchActivityFiles();
    },
    closeFileManagementModal: function() {
      this.showFileManagementModal = false;
      this.files = [];
      this.selectedFile = null;
    },
    openSendNotificationModal() {
      this.showSendNotificationModal = true;
    },
    closeSendNotificationModal() {
      this.showSendNotificationModal = false;
      this.notificationContent = '';
    },
    createActivity: function() {
      const newActivityData = {
        uid: this.getCookie('uid'),
        actName: this.newActivity.activityName,
        actDescription: this.newActivity.activityDescription,
        beginTime: this.newActivity.beginTime,
        endTime: this.newActivity.endTime,
        roleList: this.newActivity.roleList,
        ifFileStore: this.newActivity.ifFileStore,
        actStatus: this.newActivity.actStatus
      };
  
      const url = 'http://47.93.254.31:18088/activity/createActivity';
  
      axios.post(url, newActivityData)
        .then(response => {
          if (response.data.code === 1) {
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
    },

    addRole: function() {
      if (this.newRole.name) {
        this.$set(this.newActivity.roleList, this.newRole.name, 0);
        this.newRole = { name: '' };
      }
    },    
    removeRole: function(role) {
      this.$delete(this.newActivity.roleList, role);
    },
    addStatus: function() {
      if (this.newStatus.name && this.newStatus.order) {
        this.$set(this.newActivity.actStatus, this.newStatus.name, this.newStatus.order);
        this.newStatus = { name: '', order: '' };
      }
    },
    removeStatus: function(status) {
      this.$delete(this.newActivity.actStatus, status);
    },
    applyTemplate: function() {
      if (this.selectedTemplate === 'meeting') {
        this.newActivity.roleList = { '会议组织者': 1, '主讲嘉宾': 2, '参与者': 20 };
        this.newActivity.actStatus = { '会议主持人开场': 1, '会议进行中': 2, '会议结束': 3 };
      } else if (this.selectedTemplate === 'classMeeting') {
        this.newActivity.roleList = { 班主任: "", 班长: "", 同学: "" };
        this.newActivity.actStatus = { "班主任发言": 1, "班会进行中": 2, "班会结束": 3 };
      } else if (this.selectedTemplate === 'sportsMeet') { 
        this.newActivity.roleList = { 主持人: "", 运动员: "", 观众: "" };
        this.newActivity.actStatus = { "开幕式": 1, "运动会进行中": 2, "闭幕式": 3 };
      } else  {
        this.newActivity.roleList = {};
        this.newActivity.actStatus = {};
      }
    },

    updateActivity: function() {
      const updateActivityData = {
        id: this.editActivity.id,
        uid: this.editActivity.uid,
        actName: this.editActivity.actName,
        actDescription: this.editActivity.actDescription,
        beginTime: this.editActivity.beginTime,
        endTime: this.editActivity.endTime,
        totalBudget: this.editActivity.totalBudget,
        place: this.editActivity.place,
        ifFileStore: this.editActivity.ifFileStore,
        rank: this.editActivity.rank,
        userCost: this.editActivity.userCost,
        userGroup: this.editActivity.userGroup,
        resource: this.editActivity.resource,
        userList: this.editActivity.userList,
        roleList: this.editActivity.roleList,
        actStatus: this.editActivity.actStatus
      };
      
      axios.post('http://47.93.254.31:18088/activity/updateActivity', updateActivityData)
        .then(response => {
          if (response.data.code === 1) {
            this.showMessage('活动更新成功');
            this.closeEditActivityModal();
            this.fetchData();
          } else {
            this.showMessage('活动更新失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('活动更新失败：', error);
          this.showMessage('活动更新失败');
        });
    },
    setParticipantGroup: function() {
      const groupData = {
        managerUid: this.getCookie('uid'),
        aid: this.selectedActivityId,
        participantUid: this.participantUid,
        group: this.participantGroup
      };
      axios.post('http://47.93.254.31:18088/activity/setParticipantGroup', groupData)
        .then(response => {
          if (response.data.code === 1) {
            this.showMessage('参与者分组设置成功');
            this.closeParticipantManagementModal();
            this.fetchData();
          } else {
            this.showMessage('参与者分组设置失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('参与者分组设置失败：', error);
          this.showMessage('参与者分组设置失败');
        });
    },
    setParticipantRole: function() {
      const roleData = {
        managerUid: this.getCookie('uid'),
        aid: this.selectedActivityId,
        participantUid: this.participantUid,
        role: this.participantRole
      };
      axios.post('http://47.93.254.31:18088/activity/setParticipantRole', roleData)
        .then(response => {
          if (response.data.code === 1) {
            this.showMessage('参与者角色设置成功');
            this.closeParticipantManagementModal();
            this.fetchData();
          } else {
            this.showMessage('参与者角色设置失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('参与者角色设置失败：', error);
          this.showMessage('参与者角色设置失败');
        });
    },
    reserveResource: function() {
      const reservationData = {
        uid: this.getCookie('uid'),
        aid: this.selectedActivityId,
        resource: this.resourceReservation.resource,
        quantity: this.resourceReservation.quantity,
      };
      axios.post('http://47.93.254.31:18088/resource/resourceReservation', reservationData)
        .then(response => {
          if (response.data.code === 1) {
            this.showMessage('资源预约成功');
            this.closeResourceReservationModal();
            this.fetchData();
          } else {
            this.showMessage('资源预约失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('资源预约失败：', error);
          this.showMessage('资源预约失败');
        });
    },
    joinActivity: function() {
      const uid = this.getCookie('uid');
      const reason = this.joinActivityReason;
      axios.get(`http://47.93.254.31:18088/activity/JoinActivity?aid=${this.selectedActivityId}&uid=${uid}&reason=${reason}`)
        .then(response => {
          if (response.data.code === 1) {
            this.showMessage('参与申请已提交');
            this.closeJoinActivityModal();
          } else {
            this.showMessage('参与申请失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('参与申请失败：', error);
          this.showMessage('参与申请失败');
        });
    },
    exitActivity: function(aid) {
      const uid = this.getCookie('uid');
      axios.get(`http://47.93.254.31:18088/activity/exitActivity?aid=${aid}&uid=${uid}`)
        .then(response => {
          if (response.data.code === 1) {
            this.showMessage('已退出活动');
            this.fetchData();
          } else {
            this.showMessage('退出活动失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('退出活动失败：', error);
          this.showMessage('退出活动失败');
        });
    },
    fetchUnCheckedUserList: function() {
      const uid = this.getCookie('uid');
      axios.post('http://47.93.254.31:18088/activity/GetUnCheckedUserList', {
        uid: uid,
        aid: this.selectedActivityId,
        page: 1,
        pageSize: 10
      })
      .then(response => {
        if (response.data.code === 1) {
          this.unCheckedUserList = response.data.data.records;
        } else {
          this.showMessage('获取待审核用户列表失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('获取待审核用户列表失败：', error);
        this.showMessage('获取待审核用户列表失败');
      });
    },
    checkApplication: function(uid, result) {
      const aid = this.selectedActivityId;
      axios.get(`http://47.93.254.31:18088/activity/CheckApplication?uid=${this.getCookie('uid')}&aid=${aid}&unCheckedId=${uid}&result=${result}`)
        .then(response => {
          if (response.data.code === 1) {
            this.showMessage('审核成功');
            this.fetchUnCheckedUserList();
          } else {
            this.showMessage('审核失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('审核失败：', error);
          this.showMessage('审核失败');
        });
    },
    setBudget: function() {
      const uid = this.getCookie('uid');
      axios.get(`http://47.93.254.31:18088/activity/setBudget?uid=${uid}&aid=${this.selectedActivityId}&budget=${this.budget}`)
        .then(response => {
          if (response.data.code === 1) {
            this.showMessage('预算设定成功');
            this.closeSetBudgetModal();
            this.fetchData();
          } else {
            this.showMessage('预算设定失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('预算设定失败：', error);
          this.showMessage('预算设定失败');
        });
    },
    fetchActivityBudget: function(aid) {
      const uid = this.getCookie('uid');
      axios.get(`http://47.93.254.31:18088/activity/getBudget?uid=${uid}&aid=${aid}`)
        .then(response => {
          if (response.data.code === 1) {
            this.showMessage(`当前活动预算为：${response.data.data}`);
          } else {
            this.showMessage('获取预算失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('获取预算失败：', error);
          this.showMessage('获取预算失败');
        });
    },
    commitExpense: function() {
      const expenseData = {
        uid: this.getCookie('uid'),
        aid: this.selectedActivityId,
        content: this.expense.content,
        cost: this.expense.cost
      };
      axios.post('http://47.93.254.31:18088/expense/commitExpense', expenseData)
        .then(response => {
          if (response.data.code === 1) {
            this.showMessage('报销单提交成功');
            this.closeCommitExpenseModal();
            this.fetchData();
          } else {
            this.showMessage('报销单提交失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('报销单提交失败：', error);
          this.showMessage('报销单提交失败');
        });
    },
    fetchUserExpenses: function() {
      const uid = this.getCookie('uid');
      axios.post('http://47.93.254.31:18088/expense/getExpenseListToUser', {
        uid: uid,
        page: this.expensePage,
        pageSize: this.expensePageSize
      })
      .then(response => {
        if (response.data.code === 1) {
          this.userExpenses = response.data.data.records;
          this.expenseTotal = response.data.data.total;
        } else {
          this.showMessage('获取报销单列表失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('获取报销单列表失败：', error);
        this.showMessage('获取报销单列表失败');
      });
    },
    setRank: function(aid, rank) {
      const uid = this.getCookie('uid');
      axios.get(`http://47.93.254.31:18088/activity/setRankForAct?uid=${uid}&aid=${aid}&rank=${rank}`)
        .then(response => {
          if (response.data.code === 1) {
            this.showMessage('评分提交成功');
          } else {
            this.showMessage('评分提交失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('评分提交失败：', error);
          this.showMessage('评分提交失败');
        });
    },
    fetchActivityExpenses: function() {
      const uid = this.getCookie('uid');
      axios.post('http://47.93.254.31:18088/expense/getExpenseListToManager', {
        uid: uid,
        aid: this.selectedActivityId,
        page: this.expensePage,
        pageSize: this.expensePageSize
      })
      .then(response => {
        if (response.data.code === 1) {
          this.activityExpenses = response.data.data.records;
          this.expenseTotal = response.data.data.total;
        } else {
          this.showMessage('获取活动报销单列表失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('获取活动报销单列表失败：', error);
        this.showMessage('获取活动报销单列表失败');
      });
    },
    checkExpense: function(eid, status, comment) {
      const uid = this.getCookie('uid');
      axios.post('http://47.93.254.31:18088/expense/checkExpense', {
        eid: eid,
        aid: this.selectedActivityId,
        uid: uid,
        status: status,
        comment: comment
      })
      .then(response => {
        if (response.data.code === 1) {
          this.showMessage('报销单审核成功');
          this.fetchActivityExpenses();
        } else {
          this.showMessage('报销单审核失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('报销单审核失败：', error);
        this.showMessage('报销单审核失败');
      });
    },
    rejectExpense: function(eid) {
      const comment = prompt("请输入拒绝理由：");
      if (comment !== null) {
        this.checkExpense(eid, -1, comment);
      }
    },
    changePage: function(newPage) {
      this.page = newPage;
      this.fetchData();
    },
    changeExpensePage: function(newPage) {
      this.expensePage = newPage;
      this.fetchActivityExpenses();
    },
    sendNotification() {
      const notificationData = {
        aid: this.selectedActivity.id,
        sendUid: this.userProfile.id,
        type: 1,
        content: this.notificationContent
      };
  
      axios.post('http://47.93.254.31:18088/user/createNotice', notificationData)
        .then(response => {
          if (response.data.code === 1) {
            this.showMessage('通知发送成功');
            this.closeSendNotificationModal();
          } else {
            this.showMessage('通知发送失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('通知发送失败：', error);
          this.showMessage('通知发送失败');
        });
    },
    fetchUserNotifications: function() {
      const uid = this.getCookie('uid');
      axios.post('http://47.93.254.31:18088/user/getNoticeToUser', {
        uid: uid,
        page: this.notificationPage,
        pageSize: this.notificationPageSize
      })
      .then(response => {
        if (response.data.code === 1) {
          this.userNotifications = response.data.data.records;
          this.notificationTotal = response.data.data.total;
        } else {
          this.showMessage('获取通知失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('获取通知失败：', error);
        this.showMessage('获取通知失败');
      });
    },
    fetchActivityNotifications: function() {
      const uid = this.getCookie('uid');
      axios.post('http://47.93.254.31:18088/user/getNoticeToManager', {
        uid: uid,
        aid: this.selectedActivityId,
        page: this.notificationPage,
        pageSize: this.notificationPageSize
      })
      .then(response => {
        if (response.data.code === 1) {
          this.activityNotifications = response.data.data.records;
          this.notificationTotal = response.data.data.total;
        } else {
          this.showMessage('获取活动通知失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('获取活动通知失败：', error);
        this.showMessage('获取活动通知失败');
      });
    },
    openActivityNotificationsModal: function(aid) {
      this.selectedActivityId = aid;
      this.showActivityNotificationsModal = true;
      this.fetchActivityNotifications();
    },

    closeActivityNotificationsModal: function() {
      this.showActivityNotificationsModal = false;
    },
    openNotification: function(nid) {
      axios.get(`http://47.93.254.31:18088/user/openNotice?nid=${nid}`)
      .then(response => {
        if (response.data.code === 1) {
          this.showMessage('通知已读');
          this.fetchUserNotifications();
        } else {
          this.showMessage('确认通知失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('确认通知失败：', error);
        this.showMessage('确认通知失败');
      });
    },
    changeNotificationPage: function(newPage) {
      this.notificationPage = newPage;
      this.fetchUserNotifications();
    },
    fetchActivityFiles: function() {
      const uid = this.getCookie('uid');
      axios.post('http://47.93.254.31:18088/file/getAllFile', {
        aid: this.selectedActivityId,
        uid: uid,
        page: this.filePage,
        pageSize: this.filePageSize
      })
      .then(response => {
        if (response.data.code === 1) {
          this.files = response.data.data.records;
          this.fileTotal = response.data.data.total;
        } else {
          this.showMessage('获取文件列表失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('获取文件列表失败：', error);
        this.showMessage('获取文件列表失败');
      });
    },
    handleFileUpload: function(event) {
      this.selectedFile = event.target.files[0];
    },
    uploadFile: function() {
      const formData = new FormData();
      formData.append('file', this.selectedFile);
      formData.append('aid', this.selectedActivityId);
      formData.append('uid', this.getCookie('uid'));

      axios.post('http://47.93.254.31:18088/file/upload', formData)
      .then(response => {
        if (response.data.code === 1) {
          this.showMessage('文件上传成功');
          this.fetchActivityFiles();
        } else {
          this.showMessage('文件上传失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('文件上传失败：', error);
        this.showMessage('文件上传失败');
      });
    },

    downloadFile(fid, filename) {
      // const encodedFilename = encodeURIComponent(filename);
      const url = `http://47.93.254.31:18088/file/download`;
    
      axios.post(url, {
        aid: this.selectedActivityId,
        fid: fid,
        fileName: filename
      }, {
        responseType: 'blob'
      })
      .then(response => {
        if (response.status === 200) {
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', filename);
          document.body.appendChild(link);
          link.click();
          link.parentNode.removeChild(link);
        } else {
          this.showMessage('文件下载失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('文件下载失败：', error);
        this.showMessage('文件下载失败');
      });
    },
    
    
    deleteFile: function(fid) {
      const uid = this.getCookie('uid');
      axios.get(`http://47.93.254.31:18088/file/deleteFile?aid=${this.selectedActivityId}&fid=${fid}&uid=${uid}`)
      .then(response => {
        if (response.data.code === 1) {
          this.showMessage('文件删除成功');
          this.fetchActivityFiles();
        } else {
          this.showMessage('文件删除失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('文件删除失败：', error);
        this.showMessage('文件删除失败');
      });
    },
    addReminder: function() {
      const reminderData = {
        uid: this.getCookie('uid'),
        content: this.newReminder.content,
        reminderTime: this.newReminder.reminderTime
      };
      axios.post('http://47.93.254.31:18088/reminder/setReminder', reminderData)
      .then(response => {
        if (response.data.code === 1) {
          this.showMessage('提醒添加成功');
          this.fetchUserReminders();
        } else {
          this.showMessage('提醒添加失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('提醒添加失败：', error);
        this.showMessage('提醒添加失败');
      });
    },
    fetchUserReminders: function() {
      const uid = this.getCookie('uid');
      axios.post('http://47.93.254.31:18088/reminder/getReminderList', {
        uid: uid,
        page: this.reminderPage,
        pageSize: this.reminderPageSize
      })
      .then(response => {
        if (response.data.code === 1) {
          this.reminders = response.data.data.records;
          this.reminderTotal = response.data.data.total;
        } else {
          this.showMessage('获取提醒列表失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('获取提醒列表失败：', error);
        this.showMessage('获取提醒列表失败');
      });
    },
    deleteReminder: function(reminderId) {
      const uid = this.getCookie('uid');
      axios.get(`http://47.93.254.31:18088/reminder/deleteReminder?uid=${uid}&reminderId=${reminderId}`)
      .then(response => {
        if (response.data.code === 1) {
          this.showMessage('提醒删除成功');
          this.fetchUserReminders();
        } else {
          this.showMessage('提醒删除失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('提醒删除失败：', error);
        this.showMessage('提醒删除失败');
      });
    },
    changeFilePage: function(newPage) {
      this.filePage = newPage;
      this.fetchActivityFiles();
    },
    changeReminderPage: function(newPage) {
      this.reminderPage = newPage;
      this.fetchUserReminders();
    },
    addComment: function() {
      const commentData = {
        uid: this.getCookie('uid'),
        aid: this.selectedActivity.id,
        content: this.newComment.content,
        commentTime: new Date().toISOString()
      };
      axios.post('http://47.93.254.31:18088/comment/addComment', commentData)
      .then(response => {
        if (response.data.code === 1) {
          this.showMessage('评论添加成功');
          this.fetchComments(this.selectedActivity.id);
          this.newComment.content = '';
        } else {
          this.showMessage('评论添加失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('评论添加失败：', error);
        this.showMessage('评论添加失败');
      });
    },
    fetchComments: function(aid) {
      const uid = this.getCookie('uid');
      axios.post('http://47.93.254.31:18088/comment/getCommentList', {
        uid: uid,
        aid: aid,
        page: this.commentPage,
        pageSize: this.commentPageSize
      })
      .then(response => {
        if (response.data.code === 1) {
          this.comments = response.data.data.records;
          this.commentTotal = response.data.data.total;
        } else {
          this.showMessage('获取评论列表失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('获取评论列表失败：', error);
        this.showMessage('获取评论列表失败');
      });
    },
    likeComment: function(cid) {
      const uid = this.getCookie('uid');
      axios.get(`http://47.93.254.31:18088/comment/addLikes?cid=${cid}&uid=${uid}`)
      .then(response => {
        if (response.data.code === 1) {
          this.showMessage('点赞成功');
          this.fetchComments(this.selectedActivity.id);
        } else {
          this.showMessage('点赞失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('点赞失败：', error);
        this.showMessage('点赞失败');
      });
    },
    unlikeComment: function(cid) {
      const uid = this.getCookie('uid');
      axios.get(`http://47.93.254.31:18088/comment/undoLikes?cid=${cid}&uid=${uid}`)
      .then(response => {
        if (response.data.code === 1) {
          this.showMessage('取消点赞成功');
          this.fetchComments(this.selectedActivity.id);
        } else {
          this.showMessage('取消点赞失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('取消点赞失败：', error);
        this.showMessage('取消点赞失败');
      });
    },
    deleteComment: function(cid) {
      const uid = this.getCookie('uid');
      axios.get(`http://47.93.254.31:18088/comment/deleteComment?cid=${cid}&uid=${uid}`)
      .then(response => {
        if (response.data.code === 1) {
          this.showMessage('评论删除成功');
          this.fetchComments(this.selectedActivity.id);
        } else {
          this.showMessage('评论删除失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('评论删除失败：', error);
        this.showMessage('评论删除失败');
      });
    },
    generateActivityReport: function(aid) {
      const uid = this.getCookie('uid');
      axios.get(`http://47.93.254.31:18088/activity/getActReport?aid=${aid}&uid=${uid}`)
      .then(response => {
        if (response.data.code === 1) {
          const report = response.data.data;
          let reportContent = `
            活动名称: ${report.actName}\n
            组织者: ${report.orgName}\n
            评分: ${report.rank}\n
            参与人数: ${report.participantNum}\n
            评论数: ${report.messageNum}\n
            活动状态: ${report.actStatus.join(', ')}\n
            高点赞评论:\n
          `;
          report.highLikesCommentList.forEach((comment, index) => {
            reportContent += `${index + 1}. ${comment}\n`;
          });
          this.showMessage(reportContent);
        } else {
          this.showMessage('生成活动报告失败：' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('生成活动报告失败：', error);
        this.showMessage('生成活动报告失败');
      });
    },

    copyActivityLink: function(activity) {
      const link = `活动名称: ${activity.actName}
                    活动描述: ${activity.actDescription}
                    开始时间: ${this.formatDateTime(activity.beginTime)}
                    结束时间: ${this.formatDateTime(activity.endTime)}
                    组织者用户名: ${activity.username}
                    总预算: ${activity.totalBudget}
                    评分: ${activity.rank}
                    链接: ${window.location.href}`;
      this.copyTextToClipboard(link);
      this.showMessage('链接已复制到剪贴板');
    },

    copyTextToClipboard: function(text) {
      const textarea = document.createElement('textarea');
      textarea.value = text;
      document.body.appendChild(textarea);
      textarea.select();
      document.execCommand('copy');
      document.body.removeChild(textarea);
    }

  },
  mounted: function() {
    // 添加请求拦截器，在发送请求之前添加Authorization和uid
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
    this.fetchData();
    this.fetchUserSchedule();
  }
});
