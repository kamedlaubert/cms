'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('consultation', {
                parent: 'entity',
                url: '/consultations',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.consultation.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/consultation/consultations.html',
                        controller: 'ConsultationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('consultation');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('consultation.detail', {
                parent: 'entity',
                url: '/consultation/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.consultation.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/consultation/consultation-detail.html',
                        controller: 'ConsultationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('consultation');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Consultation', function($stateParams, Consultation) {
                        return Consultation.get({id : $stateParams.id});
                    }]
                }
            })
            .state('consultation.new', {
                parent: 'consultation',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/consultation/consultation-dialog.html',
                        controller: 'ConsultationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {motif: null, date: null, diagnostiqueConsul: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('consultation', null, { reload: true });
                    }, function() {
                        $state.go('consultation');
                    })
                }]
            })
            .state('consultation.edit', {
                parent: 'consultation',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/consultation/consultation-dialog.html',
                        controller: 'ConsultationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Consultation', function(Consultation) {
                                return Consultation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('consultation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
