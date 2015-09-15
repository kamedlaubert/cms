'use strict';

angular.module('cmsApp')
    .factory('SymptomeSearch', function ($resource) {
        return $resource('api/_search/symptomes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
