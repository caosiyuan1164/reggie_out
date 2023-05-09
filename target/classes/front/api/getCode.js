function getCode(params) {
    return $axios({
        'url': `/user/getCode/${params}`,
        'method': 'post',
        //params: { phone: params }
    })
}