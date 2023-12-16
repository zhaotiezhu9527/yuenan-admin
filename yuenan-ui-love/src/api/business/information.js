import request from '@/utils/request'

// 查询收集用户列表列表
export function listInformation(query) {
  return request({
    url: '/business/information/list',
    method: 'get',
    params: query
  })
}

// 查询收集用户列表详细
export function getInformation(id) {
  return request({
    url: '/business/information/' + id,
    method: 'get'
  })
}

// 新增收集用户列表
export function addInformation(data) {
  return request({
    url: '/business/information',
    method: 'post',
    data: data
  })
}

// 修改收集用户列表
export function updateInformation(data) {
  return request({
    url: '/business/information',
    method: 'put',
    data: data
  })
}

// 删除收集用户列表
export function delInformation(id) {
  return request({
    url: '/business/information/' + id,
    method: 'delete'
  })
}
