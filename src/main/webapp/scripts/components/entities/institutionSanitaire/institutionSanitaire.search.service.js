'use strict';

angular.module('cmsApp')
    .factory('InstitutionSanitaireSearch', function ($resource) {
        return $resource('api/_search/institutionSanitaires/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
