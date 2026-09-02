//写死的假数据：模拟后端返回的老人登录用户、体检套餐、体检项目
//后续对接后端时，把api层换成request即可

//登录用的假账号
export const mockUser = {
  id: 1,
  username: 'elder',
  password: '123456',
  name: '张桂兰',
  avatar: '',
  gender: 2,
  age: 68,
  phone: '13800138000',
  idCard: '370102195808080000',
  address: '阳光花园小区3号楼2单元101',
  room: 'A栋302'
}

//体检项目假数据
export const mockExamItems = [
  {id: 1, name: '血常规', price: 25, unit: '次', resultType: 1, referenceMin: null, referenceMax: null, referenceUnit: '', description: '检测红细胞、白细胞、血小板等血液基本指标'},
  {id: 2, name: '尿常规', price: 20, unit: '次', resultType: 1, referenceMin: null, referenceMax: null, referenceUnit: '', description: '检测尿液成分，筛查泌尿系统疾病'},
  {id: 3, name: '肝功能', price: 60, unit: '次', resultType: 1, referenceMin: 0, referenceMax: 40, referenceUnit: 'U/L', description: '检测转氨酶等肝脏代谢指标'},
  {id: 4, name: '肾功能', price: 55, unit: '次', resultType: 1, referenceMin: null, referenceMax: null, referenceUnit: '', description: '检测肌酐、尿素氮等肾脏指标'},
  {id: 5, name: '空腹血糖', price: 15, unit: '次', resultType: 1, referenceMin: 3.9, referenceMax: 6.1, referenceUnit: 'mmol/L', description: '检测空腹状态下血液中葡萄糖浓度'},
  {id: 6, name: '血脂四项', price: 45, unit: '次', resultType: 1, referenceMin: null, referenceMax: null, referenceUnit: '', description: '检测胆固醇、甘油三酯等血脂指标'},
  {id: 7, name: '心电图', price: 30, unit: '次', resultType: 0, referenceMin: null, referenceMax: null, referenceUnit: '', description: '记录心脏电活动，筛查心律失常'},
  {id: 8, name: '胸部DR', price: 80, unit: '次', resultType: 0, referenceMin: null, referenceMax: null, referenceUnit: '', description: '胸部X光检查，筛查肺部疾病'},
  {id: 9, name: '腹部B超', price: 90, unit: '次', resultType: 0, referenceMin: null, referenceMax: null, referenceUnit: '', description: '检查肝胆胰脾肾等腹部脏器'},
  {id: 10, name: '骨密度检测', price: 100, unit: '次', resultType: 1, referenceMin: -1, referenceMax: 1, referenceUnit: 'T值', description: '检测骨量，筛查骨质疏松'},
  {id: 11, name: '颈部血管B超', price: 120, unit: '次', resultType: 0, referenceMin: null, referenceMax: null, referenceUnit: '', description: '检查颈动脉斑块及狭窄情况'},
  {id: 12, name: '心脏彩超', price: 150, unit: '次', resultType: 0, referenceMin: null, referenceMax: null, referenceUnit: '', description: '检查心脏结构与功能'},
  {id: 13, name: '幽门螺杆菌检测', price: 70, unit: '次', resultType: 0, referenceMin: null, referenceMax: null, referenceUnit: '', description: '碳13/14呼气试验，筛查胃部感染'},
  {id: 14, name: '肿瘤标志物筛查', price: 200, unit: '次', resultType: 1, referenceMin: 0, referenceMax: 35, referenceUnit: 'ng/mL', description: '血液肿瘤标志物联合筛查'},
  {id: 15, name: '内科检查', price: 20, unit: '次', resultType: 0, referenceMin: null, referenceMax: null, referenceUnit: '', description: '医生问诊、血压、心肺听诊等'},
  {id: 16, name: '眼科检查', price: 25, unit: '次', resultType: 0, referenceMin: null, referenceMax: null, referenceUnit: '', description: '视力、眼底检查，筛查白内障'},
  {id: 17, name: '认知功能评估', price: 60, unit: '次', resultType: 1, referenceMin: 0, referenceMax: 30, referenceUnit: '分', description: 'MMSE量表评估认知功能状态'}
]

//体检套餐假数据
export const mockExamPackages = [
  {
    id: 1,
    name: '基础体检套餐',
    price: 199,
    image: '',
    description: '适合日常健康监测，涵盖血常规、尿常规、心电图等基础检查',
    status: 1,
    sort: 1,
    gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', //卡片渐变背景
    examItemIds: [1, 2, 5, 7, 15]
  },
  {
    id: 2,
    name: '全面体检套餐',
    price: 399,
    image: '',
    description: '在基础套餐上增加肝肾功能、血脂血糖、腹部B超等深度检查',
    status: 1,
    sort: 2,
    gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    examItemIds: [1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 15]
  },
  {
    id: 3,
    name: '深度体检套餐',
    price: 699,
    image: '',
    description: '全面体检加肿瘤标志物、心脑血管、骨密度等专项筛查，适合年度深度体检',
    status: 1,
    sort: 3,
    gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    examItemIds: [1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 14, 15, 16, 17]
  }
]

//首页健康数据假数据
export const mockHealthData = {
  bloodPressure: '132/85',
  heartRate: 76,
  bloodSugar: 5.8,
  steps: 3200,
  sleepHours: 7.5
}

//首页公告假数据
export const mockNotices = [
  {id: 1, title: '本周五上午社区免费测血压活动，欢迎参加', date: '08-28'},
  {id: 2, title: '秋季流感疫苗接种开始预约，请到前台登记', date: '08-26'},
  {id: 3, title: '体检报告已出，可在"我的预约"中查看结果', date: '08-25'},
  {id: 4, title: '社区食堂本周新增营养粥品，适合老年朋友', date: '08-24'}
]
