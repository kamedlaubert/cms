'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hospitalisation', {
                parent: 'entity',
                url: '/hospitalisations',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.hospitalisation.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hospitalisation/hospitalisations.html',
                        controller: 'HospitalisationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('hospitalisation');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('hospitalisation.detail', {
                parent: 'entity',
                url: '/hospitalisation/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.hospitalisation.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hospitalisation/hospitalisation-detail.html',
                        controller: 'HospitalisationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('hospitalisation');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Hospitalisation', function($stateParams, Hospitalisation) {
                        return Hospitalisation.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hospitalisation.new', {
                parent: 'hospitalisation',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hospitalisation/hospitalisation-dialog.html',
                        controller: 'HospitalisationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {code: null, dateAdmission: null, dateSorti: null, motif: null, bilan: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('hospitalisation', null, { reload: true });
                    }, function() {
                        $state.go('hospitalisation');
                    })
                }]
            })
            .state('hospitalisation.edit', {
                parent: 'hospitalisation',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hospitalisation/hospitalisation-dialog.html',
                        controller: 'HospitalisationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Hospitalisation', function(Hospitalisation) {
                                return Hospitalisation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hospitalisation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
