'use strict';

angular.module('cmsApp').controller('ExamensDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Examens', 'Consultation',
        function($scope, $stateParams, $modalInstance, entity, Examens, Consultation) {

        $scope.examens = entity;
        $scope.consultations = Consultation.query();
        $scope.load = function(id) {
            Examens.get({id : id}, function(result) {
                $scope.examens = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:examensUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.examens.id != null) {
                Examens.update($scope.examens, onSaveFinished);
            } else {
                Examens.save($scope.examens, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
