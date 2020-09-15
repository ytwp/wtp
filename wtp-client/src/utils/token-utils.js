export function checkSuperAdmin(roles) {
  return roles.includes('SUPPER-ADMIN')
}

export function checkAdmin(roles) {
  return roles.includes('ADMIN')
}

export function checkUser(roles) {
  return roles.includes('USER')
}

export function checkAppPermission(appId, permission, permissions) {
  return permissions.includes(appId + '+' + permission)
}

export function checkAppAdmin(appId, permissions) {
  return permissions.includes(appId + '+ADMIN')
}
