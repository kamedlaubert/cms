'use strict';

angular.module('cmsApp')
    .factory('Hospitalisation', function ($resource, DateUtils) {
        return $resource('api/hospitalisations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateAdmission = DateUtils.convertLocaleDateFromServer(data.dateAdmission);
                    data.dateSorti = DateUtils.convertLocaleDateFromServer(data.dateSorti);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateAdmission = DateUtils.convertLocaleDateToServer(data.dateAdmission);
                    data.dateSorti = DateUtils.convertLocaleDateToServer(data.dateSorti);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateAdmission = DateUtils.convertLocaleDateToServer(data.dateAdmission);
                    data.dateSorti = DateUtils.convertLocaleDateToServer(data.dateSorti);
                    return angular.toJson(data);
                }
            }
        });
    });
