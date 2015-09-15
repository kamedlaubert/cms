'use strict';

angular.module('cmsApp')
    .factory('Personnel_ExterneSearch', function ($resource) {
        return $resource('api/_search/personnel_Externes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
