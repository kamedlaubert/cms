'use strict';

angular.module('cmsApp')
    .factory('InfirmirereSearch', function ($resource) {
        return $resource('api/_search/infirmireres/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
